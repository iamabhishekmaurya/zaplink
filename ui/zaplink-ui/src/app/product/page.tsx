import Navbar from "@/components/layout/Navbar";
import { FooterSection } from "@/components/landing/FooterSection";
import { CTASection } from "@/components/landing/CTASection";
import { ProductHero } from "@/components/product/ProductHero";
import { FeatureShowcase } from "@/components/product/FeatureShowcase";
import { AnalyticsDeepDive } from "@/components/product/AnalyticsDeepDive";
import { IntegrationUniverse } from "@/components/product/IntegrationUniverse";

export default function ProductPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1 justify-center items-center">
                <ProductHero />
                <FeatureShowcase />
                <AnalyticsDeepDive />
                <IntegrationUniverse />
                <CTASection />
            </main>
            <FooterSection />
        </div>
    );
}
