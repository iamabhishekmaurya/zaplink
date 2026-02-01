'use client';

import { motion, AnimatePresence } from 'framer-motion';
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';
import { ArrowRight, Clock } from 'lucide-react';
import Link from 'next/link';

interface Post {
    id: number;
    title: string;
    excerpt: string;
    category: string;
    author: string;
    date: string;
    readTime: string;
    color: string;
    slug: string;
}

interface PostGridProps {
    posts: Post[];
    showViewAllLink?: boolean;
}

export default function PostGrid({ posts, showViewAllLink = true }: PostGridProps) {
    return (
        <section className="py-16 px-4">
            <div className="max-w-7xl mx-auto">
                {posts.length === 0 ? (
                    <div className="text-center py-20">
                        <p className="text-muted-foreground text-lg">No articles found matching your criteria.</p>
                    </div>
                ) : (
                    <motion.div layout className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
                        <AnimatePresence>
                            {posts.map((post) => (
                                <motion.div
                                    layout
                                    key={post.id}
                                    initial={{ opacity: 0, scale: 0.9 }}
                                    animate={{ opacity: 1, scale: 1 }}
                                    exit={{ opacity: 0, scale: 0.9 }}
                                    transition={{ duration: 0.3 }}
                                >
                                    <Link href={`/blogs/${post.slug}`} className="group block h-full">
                                        <Card className="h-full border-border/50 bg-card hover:border-primary/30 transition-all duration-300 hover:shadow-xl hover:-translate-y-1 overflow-hidden flex flex-col">
                                            {/* Image Placeholder */}
                                            <div className={`h-48 w-full bg-gradient-to-br ${post.color} relative overflow-hidden`}>
                                                <div className="absolute inset-0 bg-black/10 group-hover:bg-transparent transition-colors duration-300" />
                                                <div className="absolute bottom-4 left-4">
                                                    <Badge variant="secondary" className="bg-white/20 backdrop-blur-md text-white border-white/20 hover:bg-white/30">
                                                        {post.category}
                                                    </Badge>
                                                </div>
                                            </div>

                                            <CardHeader className="space-y-2">
                                                <h3 className="text-xl font-bold leading-tight group-hover:text-primary transition-colors">
                                                    {post.title}
                                                </h3>
                                            </CardHeader>

                                            <CardContent className="flex-grow">
                                                <p className="text-muted-foreground text-sm line-clamp-3">
                                                    {post.excerpt}
                                                </p>
                                            </CardContent>

                                            <CardFooter className="pt-4 border-t border-border/50 flex items-center justify-between text-xs text-muted-foreground">
                                                <div className="flex items-center gap-2">
                                                    <span className="font-medium text-foreground">{post.author}</span>
                                                    <span>•</span>
                                                    <span>{post.date}</span>
                                                </div>
                                                <div className="flex items-center gap-1">
                                                    <Clock className="w-3 h-3" />
                                                    <span>{post.readTime}</span>
                                                </div>
                                            </CardFooter>
                                        </Card>
                                    </Link>
                                </motion.div>
                            ))}
                        </AnimatePresence>
                    </motion.div>
                )}

                {showViewAllLink && (
                    <div className="flex justify-center mt-16">
                        <Link href="/blogs/articles" className="inline-flex items-center justify-center gap-2 text-primary font-medium hover:underline">
                            View all articles <ArrowRight className="w-4 h-4" />
                        </Link>
                    </div>
                )}
            </div>
        </section>
    );
}
