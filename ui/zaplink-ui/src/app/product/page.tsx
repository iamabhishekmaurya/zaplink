import Navbar from "@/components/layout/Navbar";
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { CTASection } from '@/features/landing/ui/CTASection';
import { ProductHero } from '@/features/product/ui/ProductHero';
import { FeatureShowcase } from '@/features/product/ui/FeatureShowcase';
import { AnalyticsDeepDive } from '@/features/product/ui/AnalyticsDeepDive';
import { IntegrationUniverse } from '@/features/product/ui/IntegrationUniverse';

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
