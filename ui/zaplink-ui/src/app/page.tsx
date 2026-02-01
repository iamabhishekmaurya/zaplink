import Navbar from "@/components/layout/Navbar";

import { BenefitsSection } from '@/features/landing/ui/BenefitsSection';
import { CTASection } from '@/features/landing/ui/CTASection';
import { FAQSection } from '@/features/landing/ui/FAQSection';
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { HeroSection } from '@/features/landing/ui/HeroSection';
import { PartnersSection } from '@/features/landing/ui/PartnersSection';
import { StatsSection } from '@/features/landing/ui/StatsSection';
import { TestimonialsSection } from '@/features/landing/ui/TestimonialsSection';
import { FeatureShowcase } from '@/features/product/ui/FeatureShowcase';
import { IntegrationUniverse } from '@/features/product/ui/IntegrationUniverse';

export default function Home() {
  return (
    <div className="flex min-h-screen flex-col bg-white font-sans dark:bg-slate-950">
      <Navbar />
      <main className="flex-1">
        <HeroSection />
        <PartnersSection />
        <StatsSection />
        <BenefitsSection />
        <FeatureShowcase />
        <IntegrationUniverse />
        <TestimonialsSection />
        <FAQSection />
        <CTASection />
      </main>
      <FooterSection />
    </div>
  );
}
