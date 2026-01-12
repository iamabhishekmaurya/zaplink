import Navbar from "@/components/layout/Navbar";

import { CTASection, FeaturesSection, FooterSection, HeroSection, PricingSection, ProductTrioSection, ResourcesSection, StatsSection } from "@/components/landing-components";

export default function Home() {
  return (
    <div className="flex min-h-screen flex-col bg-zinc-50 font-sans dark:bg-black">
      <Navbar />
      {/* <main className="flex-1 w-full flex flex-col items-center justify-start bg-white dark:bg-black"> */}
      <HeroSection />
      <StatsSection />
      <ProductTrioSection />
      <FeaturesSection />
      <PricingSection />
      <ResourcesSection />
      <CTASection />
      <FooterSection />
      {/* </main> */}
    </div>
  );
}
