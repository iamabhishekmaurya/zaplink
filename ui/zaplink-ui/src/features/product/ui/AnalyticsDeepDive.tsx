'use client';

import { motion } from 'motion/react';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { BarChart, Activity, Globe, MousePointer2 } from 'lucide-react';

export const AnalyticsDeepDive = () => {
    return (
        <section className="py-24 bg-muted/20">
            <div className="max-w-7xl mx-auto px-4 md:px-6">
                <div className="flex flex-col md:flex-row gap-12 items-center">
                    <div className="md:w-1/2 order-2 md:order-1">
                        <motion.div
                            initial={{ opacity: 0, scale: 0.95 }}
                            whileInView={{ opacity: 1, scale: 1 }}
                            viewport={{ once: true }}
                            transition={{ duration: 0.8 }}
                        >
                            <Card className="bg-card w-full shadow-2xl border-primary/20">
                                <CardHeader>
                                    <div className="flex items-center justify-between">
                                        <div>
                                            <CardTitle>Traffic Overview</CardTitle>
                                            <CardDescription>Last 30 Days</CardDescription>
                                        </div>
                                        <div className="flex gap-2">
                                            <div className="px-2 py-1 bg-primary/10 rounded text-xs text-primary font-medium">Desktop</div>
                                            <div className="px-2 py-1 bg-muted rounded text-xs text-muted-foreground">Mobile</div>
                                        </div>
                                    </div>
                                </CardHeader>
                                <CardContent>
                                    {/* Animated Bar Chart */}
                                    <div className="h-64 w-full flex items-end justify-between gap-2 mt-4">
                                        {[45, 78, 52, 63, 85, 42, 68, 92, 55, 73, 48, 88].map((height, i) => (
                                            <motion.div
                                                key={i}
                                                initial={{ height: 0 }}
                                                whileInView={{ height: `${height}%` }}
                                                viewport={{ once: true }}
                                                transition={{ duration: 1, delay: i * 0.05, type: "spring" }}
                                                className="w-full bg-gradient-to-t from-primary/50 to-primary rounded-t-sm relative group"
                                            >
                                                <div className="absolute -top-8 left-1/2 -translate-x-1/2 bg-popover text-popover-foreground text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap z-10 shadow-sm border border-border">
                                                    {height * 12} Views
                                                </div>
                                            </motion.div>
                                        ))}
                                    </div>
                                    <div className="flex justify-between mt-2 text-xs text-muted-foreground">
                                        <span>Jan 1</span>
                                        <span>Jan 15</span>
                                        <span>Jan 30</span>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                    </div>

                    <div className="md:w-1/2 order-1 md:order-2 space-y-6">
                        <motion.div
                            initial={{ opacity: 0, x: 20 }}
                            whileInView={{ opacity: 1, x: 0 }}
                            viewport={{ once: true }}
                            transition={{ duration: 0.6 }}
                        >
                            <h2 className="text-3xl md:text-5xl font-bold mb-4">Deep dive into your <br /><span className="text-primary font-[family-name:var(--font-script)]">Performance</span></h2>
                            <p className="text-muted-foreground text-lg mb-8">
                                Understanding your audience is key to growth.
                                zaipmebreaks down complex data into actionable insights.
                            </p>

                            <Tabs defaultValue="location" className="w-full">
                                <TabsList className="grid w-full grid-cols-3 mb-8">
                                    <TabsTrigger value="location">Location</TabsTrigger>
                                    <TabsTrigger value="devices">Devices</TabsTrigger>
                                    <TabsTrigger value="sources">Sources</TabsTrigger>
                                </TabsList>
                                <TabsContent value="location" className="space-y-4">
                                    <div className="flex items-start gap-4 p-4 rounded-xl bg-card border border-border/50">
                                        <div className="p-3 bg-blue-100 dark:bg-blue-900/20 rounded-lg">
                                            <Globe className="w-6 h-6 text-blue-600 dark:text-blue-400" />
                                        </div>
                                        <div>
                                            <h4 className="font-semibold text-lg">Geographic Heatmaps</h4>
                                            <p className="text-sm text-muted-foreground">See exactly where your clicks are coming from down to the city level.</p>
                                        </div>
                                    </div>
                                </TabsContent>
                                <TabsContent value="devices" className="space-y-4">
                                    <div className="flex items-start gap-4 p-4 rounded-xl bg-card border border-border/50">
                                        <div className="p-3 bg-purple-100 dark:bg-purple-900/20 rounded-lg">
                                            <BarChart className="w-6 h-6 text-purple-600 dark:text-purple-400" />
                                        </div>
                                        <div>
                                            <h4 className="font-semibold text-lg">Device Breakdown</h4>
                                            <p className="text-sm text-muted-foreground">Analyze performance across mobile, tablet, and desktop devices.</p>
                                        </div>
                                    </div>
                                </TabsContent>
                                <TabsContent value="sources" className="space-y-4">
                                    <div className="flex items-start gap-4 p-4 rounded-xl bg-card border border-border/50">
                                        <div className="p-3 bg-orange-100 dark:bg-orange-900/20 rounded-lg">
                                            <MousePointer2 className="w-6 h-6 text-orange-600 dark:text-orange-400" />
                                        </div>
                                        <div>
                                            <h4 className="font-semibold text-lg">Referral Sources</h4>
                                            <p className="text-sm text-muted-foreground">Track which social platforms and websites drive the most traffic.</p>
                                        </div>
                                    </div>
                                </TabsContent>
                            </Tabs>
                        </motion.div>
                    </div>
                </div>
            </div>
        </section>
    );
};
