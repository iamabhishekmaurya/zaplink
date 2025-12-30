'use client';

import { useState } from 'react';
import Link from 'next/link';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    AreaChart,
    Area
} from 'recharts';
import {
    Plus,
    Copy,
    MoreVertical,
    ExternalLink,
    MousePointer2,
    Link as LinkIcon,
    TrendingUp,
    Clock,
    Globe2
} from 'lucide-react';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger
} from '@/components/ui/dropdown-menu';
import { motion } from 'framer-motion';
import { toast } from 'sonner';
import { CreateLinkModal } from '@/components/links/CreateLinkModal';

// Mock data for charts
const clickData = [
    { name: 'Mon', clicks: 400 },
    { name: 'Tue', clicks: 300 },
    { name: 'Wed', clicks: 500 },
    { name: 'Thu', clicks: 280 },
    { name: 'Fri', clicks: 590 },
    { name: 'Sat', clicks: 320 },
    { name: 'Sun', clicks: 440 },
];

const geoData = [
    { name: 'USA', value: 45 },
    { name: 'India', value: 25 },
    { name: 'UK', value: 15 },
    { name: 'Germany', value: 10 },
    { name: 'Others', value: 5 },
];

export default function DashboardPage() {
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [links] = useState([
        { id: '1', original: 'https://github.com/iamabhishekmaurya/zaplink', short: 'zap.lk/git-main', clicks: 1240, date: '2 hours ago' },
        { id: '2', original: 'https://linkedin.com/in/abhishek', short: 'zap.lk/in-profile', clicks: 856, date: '1 day ago' },
        { id: '3', original: 'https://twitter.com/zaplink_app', short: 'zap.lk/tw-main', clicks: 432, date: '3 days ago' },
    ]);

    const copyLink = (link: string) => {
        navigator.clipboard.writeText(link);
        toast.success('Link copied to clipboard');
    };

    return (
        <div className="container mx-auto px-4 py-10 flex flex-col gap-8">
            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h1 className="text-3xl font-bold font-display tracking-tight">Dashboard Overview</h1>
                    <p className="text-muted-foreground font-medium">Welcome back, here&apos;s what&apos;s happening with your links.</p>
                </div>
                <Button
                    className="gap-2 h-11 px-6 font-semibold shadow-lg shadow-primary/20 transition-all hover:shadow-primary/30"
                    onClick={() => setIsCreateModalOpen(true)}
                >
                    <Plus className="h-5 w-5" /> Create New Link
                </Button>
                <CreateLinkModal open={isCreateModalOpen} onOpenChange={setIsCreateModalOpen} />
            </div>

            {/* Quick Stats */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <Card>
                    <CardContent className="p-6 flex items-center gap-4">
                        <div className="bg-primary/10 p-3 rounded-xl rotate-12 transition-transform hover:rotate-0">
                            <MousePointer2 className="h-6 w-6 text-primary" />
                        </div>
                        <div>
                            <p className="text-sm font-bold font-display text-muted-foreground uppercase tracking-wider">Total Clicks</p>
                            <p className="text-2xl font-bold font-display">24,532</p>
                            <p className="text-xs text-green-500 font-bold flex items-center gap-1">
                                <TrendingUp className="h-3 w-3" /> +12% from last week
                            </p>
                        </div>
                    </CardContent>
                </Card>
                <Card>
                    <CardContent className="p-6 flex items-center gap-4">
                        <div className="bg-blue-500/10 p-3 rounded-xl rotate-12 transition-transform hover:rotate-0">
                            <LinkIcon className="h-6 w-6 text-blue-500" />
                        </div>
                        <div>
                            <p className="text-sm font-bold font-display text-muted-foreground uppercase tracking-wider">Active Links</p>
                            <p className="text-2xl font-bold font-display">142</p>
                            <p className="text-xs text-muted-foreground font-medium">Across 3 campaigns</p>
                        </div>
                    </CardContent>
                </Card>
                <Card>
                    <CardContent className="p-6 flex items-center gap-4">
                        <div className="bg-purple-500/10 p-3 rounded-xl rotate-12 transition-transform hover:rotate-0">
                            <Clock className="h-6 w-6 text-purple-500" />
                        </div>
                        <div>
                            <p className="text-sm font-bold font-display text-muted-foreground uppercase tracking-wider">Avg. CTR</p>
                            <p className="text-2xl font-bold font-display">4.2%</p>
                            <p className="text-xs text-green-500 font-bold flex items-center gap-1">
                                <TrendingUp className="h-3 w-3" /> +0.5% this month
                            </p>
                        </div>
                    </CardContent>
                </Card>
                <Card>
                    <CardContent className="p-6 flex items-center gap-4">
                        <div className="bg-green-500/10 p-3 rounded-xl rotate-12 transition-transform hover:rotate-0">
                            <Globe2 className="h-6 w-6 text-green-500" />
                        </div>
                        <div>
                            <p className="text-sm font-bold font-display text-muted-foreground uppercase tracking-wider">Unique Geo</p>
                            <p className="text-2xl font-bold font-display">28</p>
                            <p className="text-xs text-muted-foreground font-medium">Countries reached</p>
                        </div>
                    </CardContent>
                </Card>
            </div>

            {/* Charts Row */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                <Card className="lg:col-span-2 shadow-sm">
                    <CardHeader>
                        <CardTitle className="font-bold font-display tracking-tight">Click Trends</CardTitle>
                        <CardDescription className="font-medium">Daily performance of all shortened links</CardDescription>
                    </CardHeader>
                    <CardContent className="h-[350px] pt-4">
                        <ResponsiveContainer width="100%" height="100%">
                            <AreaChart data={clickData}>
                                <defs>
                                    <linearGradient id="colorClicks" x1="0" y1="0" x2="0" y2="1">
                                        <stop offset="5%" stopColor="hsl(var(--primary))" stopOpacity={0.3} />
                                        <stop offset="95%" stopColor="hsl(var(--primary))" stopOpacity={0} />
                                    </linearGradient>
                                </defs>
                                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="hsl(var(--muted)/0.5)" />
                                <XAxis
                                    dataKey="name"
                                    axisLine={false}
                                    tickLine={false}
                                    tick={{ fill: 'hsl(var(--muted-foreground))', fontSize: 12 }}
                                />
                                <YAxis
                                    axisLine={false}
                                    tickLine={false}
                                    tick={{ fill: 'hsl(var(--muted-foreground))', fontSize: 12 }}
                                />
                                <Tooltip
                                    contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1)' }}
                                />
                                <Area
                                    type="monotone"
                                    dataKey="clicks"
                                    stroke="hsl(var(--primary))"
                                    strokeWidth={3}
                                    fillOpacity={1}
                                    fill="url(#colorClicks)"
                                />
                            </AreaChart>
                        </ResponsiveContainer>
                    </CardContent>
                </Card>

                <Card className="shadow-sm">
                    <CardHeader>
                        <CardTitle className="font-bold font-display tracking-tight">Top Referrers</CardTitle>
                        <CardDescription className="font-medium">Traffic sources for your links</CardDescription>
                    </CardHeader>
                    <CardContent className="flex flex-col gap-6 pt-4">
                        {[
                            { name: 'Direct', val: 75, color: 'bg-primary' },
                            { name: 'Twitter', val: 45, color: 'bg-blue-400' },
                            { name: 'LinkedIn', val: 32, color: 'bg-blue-700' },
                            { name: 'Facebook', val: 18, color: 'bg-blue-600' },
                            { name: 'Others', val: 12, color: 'bg-gray-400' },
                        ].map((ref, i) => (
                            <div key={i} className="flex flex-col gap-2">
                                <div className="flex justify-between items-center text-sm">
                                    <span className="font-medium">{ref.name}</span>
                                    <span className="text-muted-foreground">{ref.val}%</span>
                                </div>
                                <div className="h-2 w-full bg-muted rounded-full overflow-hidden">
                                    <motion.div
                                        initial={{ width: 0 }}
                                        animate={{ width: `${ref.val}%` }}
                                        transition={{ duration: 1, delay: i * 0.1 }}
                                        className={`h-full ${ref.color}`}
                                    />
                                </div>
                            </div>
                        ))}
                    </CardContent>
                </Card>
            </div>

            {/* Links Table/List */}
            <Card className="shadow-sm">
                <CardHeader className="flex flex-row items-center justify-between">
                    <div>
                        <CardTitle className="font-bold font-display tracking-tight">Recent Links</CardTitle>
                        <CardDescription className="font-medium">Manage your most recently created short links</CardDescription>
                    </div>
                    <Button asChild variant="outline" size="sm">
                        <Link href="/dashboard/links">View All</Link>
                    </Button>
                </CardHeader>
                <CardContent>
                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead>
                                <tr className="border-b text-xs font-bold font-display text-muted-foreground uppercase tracking-wider">
                                    <th className="pb-4 pl-2">Original & Short Link</th>
                                    <th className="pb-4">Clicks</th>
                                    <th className="pb-4">Created</th>
                                    <th className="pb-4 text-right pr-2">Action</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y">
                                {links.map((link) => (
                                    <tr key={link.id} className="group hover:bg-muted/30 transition-colors">
                                        <td className="py-4 pl-2">
                                            <div className="flex flex-col gap-1">
                                                <span className="font-bold text-primary flex items-center gap-1.5 leading-none">
                                                    {link.short} <ExternalLink className="h-3 w-3 inline cursor-pointer hover:opacity-100 opacity-0 group-hover:opacity-50" />
                                                </span>
                                                <span className="text-xs text-muted-foreground truncate max-w-[250px] md:max-w-[400px]">
                                                    {link.original}
                                                </span>
                                            </div>
                                        </td>
                                        <td className="py-4 font-semibold text-sm">{link.clicks.toLocaleString()}</td>
                                        <td className="py-4 text-sm text-muted-foreground">{link.date}</td>
                                        <td className="py-4 text-right pr-2">
                                            <div className="flex justify-end gap-2">
                                                <Button
                                                    variant="ghost"
                                                    size="icon"
                                                    className="h-8 w-8 rounded-full"
                                                    onClick={() => copyLink(link.short)}
                                                >
                                                    <Copy className="h-4 w-4" />
                                                </Button>
                                                <DropdownMenu>
                                                    <DropdownMenuTrigger asChild>
                                                        <Button variant="ghost" size="icon" className="h-8 w-8 rounded-full">
                                                            <MoreVertical className="h-4 w-4" />
                                                        </Button>
                                                    </DropdownMenuTrigger>
                                                    <DropdownMenuContent align="end">
                                                        <DropdownMenuItem>View Analytics</DropdownMenuItem>
                                                        <DropdownMenuItem>Edit Link</DropdownMenuItem>
                                                        <DropdownMenuItem>Reset Stats</DropdownMenuItem>
                                                        <DropdownMenuItem className="text-destructive focus:text-destructive">Delete</DropdownMenuItem>
                                                    </DropdownMenuContent>
                                                </DropdownMenu>
                                            </div>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}
