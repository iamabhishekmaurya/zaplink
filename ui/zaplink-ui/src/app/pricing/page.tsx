import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/components/landing/FooterSection';
import { CTASection } from '@/components/landing/CTASection';
import PricingHero from '@/components/pricing/PricingHero';
import PricingTiers from '@/components/pricing/PricingTiers';
import ComparisonTable from '@/components/pricing/ComparisonTable';
import PricingFAQ from '@/components/pricing/PricingFAQ';

export default function PricingPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1">
                <PricingHero />
                <PricingTiers />
                <ComparisonTable />
                <PricingFAQ />
                <CTASection />
            </main>
            <FooterSection />
        </div>
    );
}
