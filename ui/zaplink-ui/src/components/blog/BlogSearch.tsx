'use client';

import { Search } from 'lucide-react';
import { Input } from '@/components/ui/input';
import { cn } from '@/lib/utils';

const categories = ['All', 'Product', 'Engineering', 'Design', 'Marketing', 'Data', 'Growth', 'Case Study'];

interface BlogSearchProps {
    activeCategory: string;
    setActiveCategory: (category: string) => void;
    searchQuery: string;
    setSearchQuery: (query: string) => void;
}

export default function BlogSearch({ activeCategory, setActiveCategory, searchQuery, setSearchQuery }: BlogSearchProps) {
    return (
        <section className="sticky top-16 z-40 py-4 bg-background/80 backdrop-blur-md border-b border-border/50 transition-all">
            <div className="max-w-7xl mx-auto px-4 md:px-6 flex flex-col md:flex-row items-center justify-between gap-4">

                {/* Categories */}
                <div className="flex items-center gap-2 overflow-x-auto w-full md:w-auto pb-2 md:pb-0 scrollbar-hide">
                    {categories.map((category) => (
                        <button
                            key={category}
                            onClick={() => setActiveCategory(category)}
                            className={cn(
                                "px-4 py-2 rounded-full text-sm font-medium transition-all duration-200 whitespace-nowrap",
                                activeCategory === category
                                    ? "bg-primary text-primary-foreground shadow-md shadow-primary/20"
                                    : "bg-muted/50 hover:bg-muted text-muted-foreground hover:text-foreground"
                            )}
                        >
                            {category}
                        </button>
                    ))}
                </div>

                {/* Search */}
                <div className="relative w-full md:w-80">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" />
                    <Input
                        placeholder="Search articles..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="pl-10 h-10 bg-muted/30 border-border/50 focus:bg-background transition-colors rounded-full"
                    />
                </div>
            </div>
        </section>
    );
}
