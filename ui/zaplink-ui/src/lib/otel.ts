import { NodeSDK } from '@opentelemetry/sdk-node';
import { OTLPTraceExporter } from '@opentelemetry/exporter-trace-otlp-http';
import { resourceFromAttributes } from '@opentelemetry/resources';
import { ATTR_SERVICE_NAME } from '@opentelemetry/semantic-conventions';
import { getNodeAutoInstrumentations } from '@opentelemetry/auto-instrumentations-node';

const SERVICE_NAME = 'zaplink-ui';
const OTLP_ENDPOINT = 'http://localhost:4318/v1/traces';

if (typeof window === 'undefined') {
    // Node.js / Server-side initialization
    console.log('Initializing OpenTelemetry for zaplink-ui (Server)...');

    const sdk = new NodeSDK({
        resource: resourceFromAttributes({
            [ATTR_SERVICE_NAME]: SERVICE_NAME,
        }),
        traceExporter: new OTLPTraceExporter({
            url: OTLP_ENDPOINT,
        }),
        instrumentations: [getNodeAutoInstrumentations()],
    });

    try {
        sdk.start();
        console.log('OpenTelemetry (Server) initialized successfully');
    } catch (error) {
        console.error('Error initializing OpenTelemetry (Server):', error);
    }

    process.on('SIGTERM', () => {
        sdk.shutdown()
            .then(() => console.log('OpenTelemetry terminated'))
            .catch((error) => console.error('Error terminating OpenTelemetry', error))
            .finally(() => process.exit(0));
    });
} else {
    // Browser / Client-side initialization
    const { WebTracerProvider } = require('@opentelemetry/sdk-trace-web');
    const { BatchSpanProcessor } = require('@opentelemetry/sdk-trace-base');
    const { registerInstrumentations } = require('@opentelemetry/instrumentation');
    const { FetchInstrumentation } = require('@opentelemetry/instrumentation-fetch');
    const { XMLHttpRequestInstrumentation } = require('@opentelemetry/instrumentation-xml-http-request');
    const { ZoneContextManager } = require('@opentelemetry/context-zone');

    console.log('Initializing OpenTelemetry for zaplink-ui (Browser)...');

    const provider = new WebTracerProvider({
        resource: resourceFromAttributes({
            [ATTR_SERVICE_NAME]: SERVICE_NAME,
        }),
    });

    provider.addSpanProcessor(new BatchSpanProcessor(new OTLPTraceExporter({
        url: OTLP_ENDPOINT,
    })));

    provider.register({
        contextManager: new ZoneContextManager(),
    });

    registerInstrumentations({
        instrumentations: [
            new FetchInstrumentation({
                propagateTraceHeaderCorsUrls: [
                    /http:\/\/localhost:8090\/.*/, // API Gateway
                ],
            }),
            new XMLHttpRequestInstrumentation({
                propagateTraceHeaderCorsUrls: [
                    /http:\/\/localhost:8090\/.*/, // API Gateway
                ],
            }),
        ],
    });

    console.log('OpenTelemetry (Browser) initialized successfully');
}

