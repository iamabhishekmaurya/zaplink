import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/components/landing/FooterSection';
import { CTASection } from '@/components/landing/CTASection';
import ResourcesHero from '@/components/resources/ResourcesHero';
import FeaturedResource from '@/components/resources/FeaturedResource';
import ResourceGrid from '@/components/resources/ResourceGrid';
import ResourcesNewsletter from '@/components/resources/ResourcesNewsletter';

export default function ResourcesPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1">
                <ResourcesHero />
                <FeaturedResource />
                <ResourceGrid />
                <ResourcesNewsletter />
                <CTASection />
            </main>
            <FooterSection />
        </div>
    );
}
