import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { CTASection } from '@/features/landing/ui/CTASection';
import PricingHero from '@/features/pricing/ui/PricingHero';
import PricingTiers from '@/features/pricing/ui/PricingTiers';
import ComparisonTable from '@/features/pricing/ui/ComparisonTable';
import PricingFAQ from '@/features/pricing/ui/PricingFAQ';

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
