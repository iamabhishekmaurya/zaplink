'use client';

import { useState } from 'react';
import BlogSearch from '@/features/blog/ui/BlogSearch';
import PostGrid from '@/features/blog/ui/PostGrid';

import { allPosts } from '@/lib/blog-data';

export default function BlogListing({ showViewAllLink = true }: { showViewAllLink?: boolean }) {
    const [activeCategory, setActiveCategory] = useState('All');
    const [searchQuery, setSearchQuery] = useState('');

    const filteredPosts = allPosts.filter((post) => {
        const matchesCategory = activeCategory === 'All' || post.category === activeCategory;
        const matchesSearch =
            post.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
            post.excerpt.toLowerCase().includes(searchQuery.toLowerCase());

        return matchesCategory && matchesSearch;
    });

    return (
        <>
            <BlogSearch
                activeCategory={activeCategory}
                setActiveCategory={setActiveCategory}
                searchQuery={searchQuery}
                setSearchQuery={setSearchQuery}
            />
            <PostGrid posts={filteredPosts} showViewAllLink={showViewAllLink} />
        </>
    );
}
