export interface BlogPost {
    id: number;
    title: string;
    excerpt: string;
    category: string;
    author: string;
    date: string;
    readTime: string;
    color: string;
    slug: string;
    content?: string; // HTML or Markdown content
}

export const allPosts: BlogPost[] = [
    {
        id: 1,
        title: '5 Strategies to Boost Your Click-Through Rates',
        excerpt: 'Learn the psychological triggers that make people click and how to apply them to your short links.',
        category: 'Marketing',
        author: 'Sarah Jenkins',
        date: 'Jan 20, 2026',
        readTime: '6 min',
        color: 'from-blue-500 to-cyan-400',
        slug: '5-strategies-to-boost-ctr',
        content: `
            <p>Click-through rate (CTR) is one of the most important metrics in digital marketing. It tells you how many people are actually interacting with your content. But how do you improve it?</p>
            <h3>1. Use Power Words</h3>
            <p>Words like "Exclusive", "Secret", "Limited", and "Free" trigger emotional responses. Use them in your link text and surrounding copy.</p>
            <h3>2. Optimize for Mobile</h3>
            <p>More than 60% of clicks come from mobile devices. Ensure your destination page is mobile-friendly, or uses will bounce immediately.</p>
            <h3>3. A/B Test Your CTAs</h3>
            <p>Don't guess what works. Test different button colors, positions, and text. zaipme's A/B testing features make this easy.</p>
            <h3>4. Keep it Short</h3>
            <p>Shorter links look more trustworthy and are easier to share. A clean URL implies a clean, safe destination.</p>
            <h3>5. Analyze Your Data</h3>
            <p>Use zaipme's analytics to see what time of day your audience is most active and schedule your posts accordingly.</p>
        `
    },
    {
        id: 2,
        title: 'The Future of QR Codes in Retail',
        excerpt: 'QR codes are back and bigger than ever. Here is how retail brands are using them to bridge physical and digital.',
        category: 'Design',
        author: 'Mike Chen',
        date: 'Jan 18, 2026',
        readTime: '4 min',
        color: 'from-emerald-500 to-teal-400',
        slug: 'future-of-qr-codes-retail',
        content: `
            <p>Once thought dead, QR codes have made a massive comeback. In retail, they are bridging the gap between the physical store and the digital experience.</p>
            <p>Brands like Nike and Zara are using QR codes on product tags to show styling tips, check inventory, and offer exclusive discounts.</p>
             <h3>Contactless Shopping</h3>
            <p>Scan to pay, scan to order, scan to view the menu. The friction of app downloads is gone; the camera app is all you need.</p>
        `
    },
    {
        id: 3,
        title: 'Understanding Link Retargeting',
        excerpt: 'A deep dive into how you can serve ads to people who clicked your links, even if they never visited your site.',
        category: 'Growth',
        author: 'Alex Rivera',
        date: 'Jan 15, 2026',
        readTime: '8 min',
        color: 'from-violet-500 to-purple-400',
        slug: 'understanding-link-retargeting',
        content: `
            <p>Link retargeting allows you to add a retargeting pixel to your short links. This means you can build a custom audience of people who clicked your content, even if that content lives on third-party sites like YouTube or Medium.</p>
            <p>This implementation dramatically lowers your CPA (Cost Per Acquisition) because you are targeting users who have already shown intent.</p>
        `
    },
    {
        id: 4,
        title: 'API Version 2.0 Migration Guide',
        excerpt: 'Moving from v1 to v2? Here is everything you need to know to ensure a smooth transition for your app.',
        category: 'Engineering',
        author: 'David Kim',
        date: 'Jan 10, 2026',
        readTime: '12 min',
        color: 'from-[#ff8904] to-red-500',
        slug: 'api-v2-migration-guide',
        content: `
            <p>We are excited to announce zaipme API v2.0! This version introduces enhanced rate limiting, more granular scopes for tokens, and support for GraphQL.</p>
            <h3>Breaking Changes</h3>
            <ul>
                <li>The <code>/v1/links</code> endpoint has been renamed to <code>/v2/urls</code>.</li>
                <li>Authentication now requires the <code>Bearer</code> prefix.</li>
            </ul>
        `
    },
    {
        id: 5,
        title: 'Building a Link Management System for Enterprise',
        excerpt: 'Case study on how a Fortune 500 company consolidated 50+ domains into a single secure link management hub.',
        category: 'Case Study',
        author: 'Lisa Park',
        date: 'Jan 05, 2026',
        readTime: '10 min',
        color: 'from-pink-500 to-rose-400',
        slug: 'enterprise-link-management-case-study',
        content: `
            <p>Managing links across a global organization is chaotic. Marketing teams use one tool, support uses another, and developers build their own scripts.</p>
            <p>In this case study, we look at how [Company X] unified their link strategy using zaipme Enterprise. They centralized 50+ domains, implemented SSO, and gained global visibility into their link performance.</p>
        `
    },
    {
        id: 6,
        title: 'The Art of the Short URL',
        excerpt: 'Does the length of your aliased link matter? We analyzed 1 million links to find the perfect alias length.',
        category: 'Data',
        author: 'Tom Wilson',
        date: 'Jan 02, 2026',
        readTime: '7 min',
        color: 'from-indigo-500 to-blue-600',
        slug: 'art-of-the-short-url',
        content: `
            <p>Is darker.com better than dark.er? We analyzed click interactions on over 1 million links to understand how URL length and readability affect user trust.</p>
            <p>The results: <strong>Readability matters more than length</strong>. A link like <code>zap.link/summer-sale</code> performs 34% better than <code>zap.link/x7y8z</code>.</p>
        `
    },
];
