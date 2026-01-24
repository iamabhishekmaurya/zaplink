import Navbar from "@/components/layout/Navbar";

import { BenefitsSection } from "@/components/landing/BenefitsSection";
import { CTASection } from "@/components/landing/CTASection";
import { FAQSection } from "@/components/landing/FAQSection";
import { FooterSection } from "@/components/landing/FooterSection";
import { HeroSection } from "@/components/landing/HeroSection";
import { PartnersSection } from "@/components/landing/PartnersSection";
import { StatsSection } from "@/components/landing/StatsSection";
import { TestimonialsSection } from "@/components/landing/TestimonialsSection";
import { FeatureShowcase } from "@/components/product/FeatureShowcase";
import { IntegrationUniverse } from "@/components/product/IntegrationUniverse";

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
