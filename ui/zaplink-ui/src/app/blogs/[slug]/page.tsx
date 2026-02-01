import { allPosts } from '@/lib/blog-data';
import { notFound } from 'next/navigation';
import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { ArrowLeft, Calendar, Clock, Share2, Facebook, Twitter, Linkedin } from 'lucide-react';
import Link from 'next/link';

interface PageProps {
    params: Promise<{
        slug: string;
    }>;
}

export default async function BlogPostPage({ params }: PageProps) {
    const { slug } = await params;
    const post = allPosts.find((p) => p.slug === slug);

    if (!post) {
        notFound();
    }

    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1 pt-24 pb-20">
                <article className="max-w-4xl mx-auto px-4 sm:px-6">

                    {/* Back Link */}
                    <div className="mb-8">
                        <Link href="/blogs" className="inline-flex items-center text-sm text-muted-foreground hover:text-primary transition-colors">
                            <ArrowLeft className="w-4 h-4 mr-2" />
                            Back to Blog
                        </Link>
                    </div>

                    {/* Header */}
                    <div className="mb-12 text-center">
                        <Badge variant="secondary" className="mb-4 bg-primary/10 text-primary hover:bg-primary/20">
                            {post.category}
                        </Badge>
                        <h1 className="text-4xl md:text-5xl lg:text-6xl font-bold tracking-tight mb-6 leading-tight">
                            {post.title}
                        </h1>

                        <div className="flex flex-wrap items-center justify-center gap-6 text-muted-foreground text-sm">
                            <div className="flex items-center gap-2">
                                <span className="w-8 h-8 rounded-full bg-muted flex items-center justify-center font-bold text-xs text-foreground">
                                    {post.author.charAt(0)}
                                </span>
                                <span className="text-foreground font-medium">{post.author}</span>
                            </div>
                            <div className="flex items-center gap-2">
                                <Calendar className="w-4 h-4" />
                                <span>{post.date}</span>
                            </div>
                            <div className="flex items-center gap-2">
                                <Clock className="w-4 h-4" />
                                <span>{post.readTime} read</span>
                            </div>
                        </div>
                    </div>

                    {/* Featured Image */}
                    <div className={`w-full h-[400px] md:h-[500px] rounded-3xl bg-gradient-to-br ${post.color} mb-16 relative overflow-hidden`}>
                        {/* Placeholder for real image */}
                        <div className="absolute inset-0 bg-black/10" />
                        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 text-white/20 text-9xl select-none">
                            ✍️
                        </div>
                    </div>

                    {/* Content Layout: Sidebar + Main Content */}
                    <div className="grid grid-cols-1 lg:grid-cols-[1fr_200px] gap-12">

                        {/* Main Content */}
                        <div className="prose prose-lg dark:prose-invert max-w-none">
                            <div dangerouslySetInnerHTML={{ __html: post.content || '' }} />

                            {/* Dummy filler content to make the page look longer and realistic */}
                            <p>
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
                            </p>
                            <blockquote>
                                <p>"The best marketing doesn't feel like marketing." — Tom Fishburne</p>
                            </blockquote>
                            <p>
                                Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
                            </p>
                            <h3>Conclusion</h3>
                            <p>
                                Start experimenting with these strategies today. If you need help, our support team is always available to guide you through the best practices for your specific use case.
                            </p>
                        </div>

                        {/* Sidebar (Share) */}
                        <div className="hidden lg:block">
                            <div className="sticky top-32 space-y-6">
                                <p className="text-sm font-semibold text-muted-foreground uppercase tracking-wider">Share</p>
                                <div className="flex flex-col gap-3">
                                    <Button variant="outline" size="icon" className="rounded-full w-10 h-10 hover:text-[#1877F2] hover:border-[#1877F2]/30">
                                        <Facebook className="w-4 h-4" />
                                    </Button>
                                    <Button variant="outline" size="icon" className="rounded-full w-10 h-10 hover:text-[#1DA1F2] hover:border-[#1DA1F2]/30">
                                        <Twitter className="w-4 h-4" />
                                    </Button>
                                    <Button variant="outline" size="icon" className="rounded-full w-10 h-10 hover:text-[#0A66C2] hover:border-[#0A66C2]/30">
                                        <Linkedin className="w-4 h-4" />
                                    </Button>
                                    <Button variant="outline" size="icon" className="rounded-full w-10 h-10">
                                        <Share2 className="w-4 h-4" />
                                    </Button>
                                </div>
                            </div>
                        </div>

                    </div>

                    {/* Mobile Share (Bottom) */}
                    <div className="lg:hidden mt-12 pt-8 border-t border-border">
                        <p className="text-center text-sm font-semibold text-muted-foreground mb-4">Share this article</p>
                        <div className="flex justify-center gap-4">
                            <Button variant="outline" size="icon" className="rounded-full"><Facebook className="w-4 h-4" /></Button>
                            <Button variant="outline" size="icon" className="rounded-full"><Twitter className="w-4 h-4" /></Button>
                            <Button variant="outline" size="icon" className="rounded-full"><Linkedin className="w-4 h-4" /></Button>
                            <Button variant="outline" size="icon" className="rounded-full"><Share2 className="w-4 h-4" /></Button>
                        </div>
                    </div>
                </article>
            </main>
            <FooterSection />
        </div>
    );
}
