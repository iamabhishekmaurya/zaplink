'use client';

import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { ArrowRight, Zap, Shield, BarChart3, Globe, Check, Facebook, Instagram, Linkedin, Twitter, Link as LinkIcon, QrCode } from "lucide-react";
import { motion } from "motion/react";
import { Particles } from "@/components/ui/particles";
import { useTheme } from "next-themes";
import { useEffect, useState } from "react";
import Link from 'next/link';
import { Card, CardContent, CardHeader, CardTitle, CardDescription, CardFooter } from "@/components/ui/card";
import { Switch } from "@/components/ui/switch";
import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from "@/components/ui/input";

// Animation Variants
const fadeInUp = {
    initial: { opacity: 0, y: 20 },
    whileInView: { opacity: 1, y: 0 },
    viewport: { once: true, margin: "-100px" },
    transition: { duration: 0.5 }
};

const staggerContainer = {
    whileInView: { transition: { staggerChildren: 0.1 } }
};

export const HeroSection = () => {
    const { resolvedTheme } = useTheme();
    const [color, setColor] = useState("#ffffff");

    useEffect(() => {
        setColor(resolvedTheme === "light" ? "#000000" : "#ffffff");
    }, [resolvedTheme]);

    return (
        <section className="relative w-full overflow-hidden pt-32 pb-20 md:pt-48 md:pb-32">
            <Particles
                className="absolute inset-0 z-0"
                quantity={80}
                ease={80}
                color={color}
                refresh
            />
            <div className="container relative z-10 mx-auto px-4 md:px-6">
                <div className="grid gap-12 lg:grid-cols-2 lg:gap-8 items-center">
                    <div className="flex flex-col justify-center space-y-8 text-center lg:text-left">
                        <motion.div
                            initial={{ opacity: 0, x: -20 }}
                            animate={{ opacity: 1, x: 0 }}
                            transition={{ duration: 0.5 }}
                        >
                            <Badge variant="outline" className="w-fit mx-auto lg:mx-0 px-4 py-1 text-sm rounded-full backdrop-blur-sm bg-background/50 border-primary/20">
                                <span className="text-primary mr-2">New</span> Zaplink Connections Platform
                            </Badge>
                        </motion.div>

                        <div className="space-y-4">
                            <motion.h1
                                className="text-4xl font-bold tracking-tighter sm:text-5xl md:text-6xl lg:text-7xl bg-clip-text text-transparent bg-gradient-to-br from-foreground to-foreground/70"
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.5, delay: 0.1 }}
                            >
                                Build stronger <br />
                                <span className="text-primary">digital connections</span>
                            </motion.h1>
                            <motion.p
                                className="max-w-[600px] mx-auto lg:mx-0 text-muted-foreground md:text-xl lg:text-lg xl:text-xl"
                                initial={{ opacity: 0, y: 20 }}
                                animate={{ opacity: 1, y: 0 }}
                                transition={{ duration: 0.5, delay: 0.2 }}
                            >
                                Use our URL shortener, QR Codes, and landing pages to engage your audience and connect them to the right information.
                            </motion.p>
                        </div>

                        <motion.div
                            className="flex flex-col gap-2 min-[400px]:flex-row justify-center lg:justify-start"
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5, delay: 0.3 }}
                        >
                            <Link href="/signup">
                                <Button size="lg" className="h-12 px-8 text-lg rounded-full shadow-lg hover:shadow-primary/25 transition-all">
                                    Get Started for Free <ArrowRight className="ml-2 h-4 w-4" />
                                </Button>
                            </Link>
                            <Link href="#pricing">
                                <Button size="lg" variant="outline" className="h-12 px-8 text-lg rounded-full backdrop-blur-sm bg-background/50 shadow-sm">
                                    View Pricing
                                </Button>
                            </Link>
                        </motion.div>
                    </div>

                    <motion.div
                        initial={{ opacity: 0, scale: 0.95 }}
                        animate={{ opacity: 1, scale: 1 }}
                        transition={{ duration: 0.5, delay: 0.2 }}
                        className="mx-auto w-full max-w-md lg:max-w-none"
                    >
                        <Card className="border-border/50 bg-background/60 backdrop-blur-xl shadow-2xl relative overflow-hidden">
                            <div className="absolute top-0 left-0 w-full h-1 bg-gradient-to-r from-primary to-purple-500" />
                            <CardHeader>
                                <CardTitle>Create a Connection</CardTitle>
                                <CardDescription>Instantly create a short link or QR code.</CardDescription>
                            </CardHeader>
                            <CardContent>
                                <Tabs defaultValue="short_link" className="w-full">
                                    <TabsList className="grid w-full grid-cols-2 mb-6">
                                        <TabsTrigger value="short_link">Short Link</TabsTrigger>
                                        <TabsTrigger value="qr_code">QR Code</TabsTrigger>
                                    </TabsList>
                                    <TabsContent value="short_link" className="space-y-4">
                                        <div className="space-y-2">
                                            <Label htmlFor="url">Paste a long URL</Label>
                                            <div className="flex gap-2">
                                                <Input id="url" placeholder="https://example.com/very/long/url..." className="h-12 bg-background/50" />
                                            </div>
                                        </div>
                                        <Button className="w-full h-12 text-lg">Shorten URL</Button>
                                        <p className="text-xs text-muted-foreground text-center">
                                            By clicking Shorten URL, you agree to Zaplink's Terms of Use.
                                        </p>
                                    </TabsContent>
                                    <TabsContent value="qr_code" className="space-y-4">
                                        <div className="space-y-2">
                                            <Label htmlFor="qr-url">Destination URL</Label>
                                            <Input id="qr-url" placeholder="https://example.com" className="h-12 bg-background/50" />
                                        </div>
                                        <Button className="w-full h-12 text-lg">Create QR Code</Button>
                                        <p className="text-xs text-muted-foreground text-center">
                                            Sign up to create custom, trackable QR codes.
                                        </p>
                                    </TabsContent>
                                </Tabs>
                            </CardContent>
                        </Card>
                    </motion.div>
                </div>
            </div>

            {/* Abstract Background Elements */}
            <div className="absolute top-0 right-0 w-[800px] h-[800px] bg-primary/5 blur-[120px] rounded-full pointer-events-none -z-10 translate-x-1/2 -translate-y-1/2" />
            <div className="absolute bottom-0 left-0 w-[600px] h-[600px] bg-purple-500/5 blur-[100px] rounded-full pointer-events-none -z-10 -translate-x-1/2 translate-y-1/2" />
        </section>
    );
}

const stats = [
    { label: "Links Created", value: "500K+" },
    { label: "Total Clicks", value: "10M+" },
    { label: "Active Users", value: "50K+" },
    { label: "Uptime", value: "99.9%" },
];

export const StatsSection = () => {
    return (
        <section className="border-y border-border/50 bg-muted/20 backdrop-blur-sm">
            <div className="container mx-auto px-4 py-12 md:px-6">
                <motion.div
                    className="grid grid-cols-2 md:grid-cols-4 gap-8 text-center"
                    initial="initial"
                    whileInView="whileInView"
                    viewport={{ once: true }}
                    variants={staggerContainer}
                >
                    {stats.map((stat, index) => (
                        <motion.div key={index} variants={fadeInUp} className="space-y-2">
                            <h3 className="text-4xl font-bold tracking-tighter text-foreground">{stat.value}</h3>
                            <p className="text-sm font-medium text-muted-foreground uppercase tracking-widest">{stat.label}</p>
                        </motion.div>
                    ))}
                </motion.div>
            </div>
        </section>
    )
}


const products = [
    {
        title: "Link Management",
        description: "A comprehensive solution to help make every point of connection between your content and your audience more powerful.",
        icon: LinkIcon,
        cta: "Get Started with Links"
    },
    {
        title: "QR Codes",
        description: "QR Code solutions for every customer, business and brand experience. Fully customizable and trackable.",
        icon: QrCode,
        cta: "Create QR Codes"
    },
    {
        title: "Zaplink Pages",
        description: "Create engaging, mobile-optimized landing pages in minutes to house all your important links.",
        icon: Zap,
        cta: "Build a Page"
    }
];

export const ProductTrioSection = () => {
    return (
        <section className="container mx-auto px-4 py-24 md:px-6">
            <motion.div
                className="text-center mb-16 space-y-4"
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5 }}
            >
                <h2 className="text-3xl font-bold tracking-tighter md:text-5xl">The Zaplink Connections Platform</h2>
                <p className="max-w-[700px] mx-auto text-muted-foreground md:text-xl">
                    All the products you need to build your brand and connect with your audience.
                </p>
            </motion.div>

            <motion.div
                className="grid gap-8 md:grid-cols-3"
                initial="initial"
                whileInView="whileInView"
                viewport={{ once: true }}
                variants={staggerContainer}
            >
                {products.map((product, index) => (
                    <motion.div
                        key={index}
                        variants={fadeInUp}
                        className="h-full"
                    >
                        <Card className="h-full flex flex-col hover:shadow-xl transition-all duration-300 border-border/50 hover:border-primary/50 relative overflow-hidden group">
                            <div className="absolute top-0 left-0 w-1 h-0 bg-primary group-hover:h-full transition-all duration-300" />
                            <CardHeader>
                                <div className="p-3 w-fit rounded-xl bg-primary/10 text-primary mb-6 group-hover:scale-110 transition-transform duration-300">
                                    <product.icon className="h-8 w-8" />
                                </div>
                                <CardTitle className="text-2xl">{product.title}</CardTitle>
                            </CardHeader>
                            <CardContent className="flex-1">
                                <p className="text-muted-foreground leading-relaxed">
                                    {product.description}
                                </p>
                            </CardContent>
                            <CardFooter>
                                <Button variant="ghost" className="group/btn p-0 hover:bg-transparent hover:text-primary">
                                    {product.cta} <ArrowRight className="ml-2 h-4 w-4 group-hover/btn:translate-x-1 transition-transform" />
                                </Button>
                            </CardFooter>
                        </Card>
                    </motion.div>
                ))}
            </motion.div>
        </section>
    )
}

const features = [
    {
        title: "Advanced Analytics",
        description: "Track clicks, location, device type, and referrers in real-time.",
        icon: BarChart3
    },
    {
        title: "Enterprise Security",
        description: "Bank-grade encryption and custom domain (SSL) support for your links.",
        icon: Shield
    },
    {
        title: "Global CDN",
        description: "Lightning fast redirects powered by our distributed global network.",
        icon: Globe
    },
    {
        title: "API Access",
        description: " Integrate Zaplink directly into your applications with our robust API.",
        icon: Zap
    }
];

export const FeaturesSection = () => {
    return (
        <section id="features" className="container mx-auto px-4 py-24 md:px-6">
            <motion.div
                className="text-center mb-16 space-y-4"
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5 }}
            >
                <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">More than just a link shortener</h2>
                <p className="max-w-[700px] mx-auto text-muted-foreground md:text-xl">
                    Powerful features to help you manage your connections at scale.
                </p>
            </motion.div>
            <motion.div
                className="grid gap-8 md:grid-cols-2 lg:grid-cols-4"
                initial="initial"
                whileInView="whileInView"
                viewport={{ once: true }}
                variants={staggerContainer}
            >
                {features.map((feature, index) => (
                    <motion.div
                        key={index}
                        variants={fadeInUp}
                    >
                        <Card className="h-full bg-background/50 backdrop-blur-sm border-border/50 hover:border-primary/30 transition-all hover:bg-muted/50">
                            <CardHeader>
                                <div className="p-3 w-fit rounded-xl bg-primary/10 text-primary mb-4">
                                    <feature.icon className="h-6 w-6" />
                                </div>
                                <CardTitle className="text-xl">{feature.title}</CardTitle>
                            </CardHeader>
                            <CardContent>
                                <p className="text-muted-foreground">
                                    {feature.description}
                                </p>
                            </CardContent>
                        </Card>
                    </motion.div>
                ))}
            </motion.div>
        </section>
    )
}

export const CTASection = () => {
    return (
        <section className="container mx-auto px-4 py-24 md:px-6">
            <motion.div
                className="rounded-3xl bg-primary/5 border border-primary/10 p-8 md:p-16 text-center overflow-hidden relative"
                initial={{ opacity: 0, scale: 0.95 }}
                whileInView={{ opacity: 1, scale: 1 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5 }}
            >
                <div className="relative z-10 max-w-2xl mx-auto space-y-6">
                    <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">
                        Ready to boost your link engagement?
                    </h2>
                    <p className="text-muted-foreground text-lg">
                        Join thousands of marketers and developers using Zaplink to manage their links.
                    </p>
                    <div className="flex flex-col gap-2 min-[400px]:flex-row justify-center pt-4">
                        <Link href="/signup">
                            <Button size="lg" className="h-12 px-8 text-lg rounded-full shadow-lg hover:shadow-primary/25 transition-all">
                                Get Started for Free
                            </Button>
                        </Link>
                    </div>
                </div>
                <div className="absolute top-0 right-0 w-[300px] h-[300px] bg-primary/10 blur-[100px] rounded-full pointer-events-none" />
                <div className="absolute bottom-0 left-0 w-[300px] h-[300px] bg-primary/10 blur-[100px] rounded-full pointer-events-none" />
            </motion.div>
        </section>
    )
}

export const PricingSection = () => {
    const [isYearly, setIsYearly] = useState(false);

    return (
        <section id="pricing" className="container mx-auto px-4 py-24 md:px-6 border-t border-border/40">
            <motion.div
                className="flex flex-col items-center justify-center space-y-4 text-center"
                initial={{ opacity: 0, y: 20 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ duration: 0.5 }}
            >
                <div className="space-y-2">
                    <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">Simple, transparent pricing</h2>
                    <p className="max-w-[700px] text-muted-foreground md:text-xl">
                        Choose the plan that's right for you. switch plans or cancel anytime.
                    </p>
                </div>
                <div className="flex items-center space-x-4">
                    <Label htmlFor="pricing-mode" className={!isYearly ? "" : "text-muted-foreground"}>Monthly</Label>
                    <Switch id="pricing-mode" checked={isYearly} onCheckedChange={setIsYearly} />
                    <Label htmlFor="pricing-mode" className={isYearly ? "" : "text-muted-foreground"}>Yearly <span className="text-green-500 text-xs ml-1 font-medium">(Save 20%)</span></Label>
                </div>
            </motion.div>
            <motion.div
                className="grid gap-8 pt-12 md:grid-cols-2 lg:grid-cols-3"
                initial="initial"
                whileInView="whileInView"
                viewport={{ once: true }}
                variants={staggerContainer}
            >
                {/* Free Plan */}
                <motion.div variants={fadeInUp}>
                    <Card className="flex flex-col h-full">
                        <CardHeader>
                            <CardTitle className="text-xl">Free</CardTitle>
                            <div className="text-3xl font-bold">$0<span className="text-lg font-normal text-muted-foreground">/mo</span></div>
                            <p className="text-sm text-muted-foreground">For individuals just starting out.</p>
                        </CardHeader>
                        <CardContent className="flex-1">
                            <ul className="grid gap-3 text-sm">
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> 50 Short Links / mo</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Basic Analytics</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> 5 QR Codes</li>
                                <li className="flex items-center gap-2 text-muted-foreground"><Check className="h-4 w-4" /> Custom Aliases</li>
                            </ul>
                        </CardContent>
                        <div className="p-6 pt-0 mt-auto">
                            <Button variant="outline" className="w-full">Get Started</Button>
                        </div>
                    </Card>
                </motion.div>

                {/* Pro Plan */}
                <motion.div variants={fadeInUp}>
                    <Card className="flex flex-col h-full border-primary shadow-lg relative overflow-hidden">
                        <div className="absolute top-0 right-0 bg-primary text-primary-foreground px-3 py-1 text-xs font-semibold rounded-bl-lg">
                            Popular
                        </div>
                        <CardHeader>
                            <CardTitle className="text-xl">Pro</CardTitle>
                            <div className="text-3xl font-bold">{isYearly ? "$12" : "$15"}<span className="text-lg font-normal text-muted-foreground">/mo</span></div>
                            <p className="text-sm text-muted-foreground">For power users and creators.</p>
                        </CardHeader>
                        <CardContent className="flex-1">
                            <ul className="grid gap-3 text-sm">
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Unlimited Short Links</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Advanced Analytics</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> 50 QR Codes</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Custom Aliases</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Password Protection</li>
                            </ul>
                        </CardContent>
                        <div className="p-6 pt-0 mt-auto">
                            <Button className="w-full shadow-lg shadow-primary/20">Upgrade to Pro</Button>
                        </div>
                    </Card>
                </motion.div>

                {/* Enterprise Plan */}
                <motion.div variants={fadeInUp}>
                    <Card className="flex flex-col h-full">
                        <CardHeader>
                            <CardTitle className="text-xl">Term</CardTitle>
                            <div className="text-3xl font-bold">{isYearly ? "$40" : "$49"}<span className="text-lg font-normal text-muted-foreground">/mo</span></div>
                            <p className="text-sm text-muted-foreground">For teams and organizations.</p>
                        </CardHeader>
                        <CardContent className="flex-1">
                            <ul className="grid gap-3 text-sm">
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Everything in Pro</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Team Collaboration</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Custom Domain (SSL)</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> Dedicated Support</li>
                                <li className="flex items-center gap-2"><Check className="h-4 w-4 text-primary" /> SSO / SAML</li>
                            </ul>
                        </CardContent>
                        <div className="p-6 pt-0 mt-auto">
                            <Button variant="outline" className="w-full">Contact Sales</Button>
                        </div>
                    </Card>
                </motion.div>
            </motion.div>
        </section>
    )
}

export const ResourcesSection = () => {
    return (
        <section id="resources" className="w-full mx-auto px-4 py-24 md:px-6 bg-muted/30">
            <div className="container mx-auto px-4 md:px-6">
                <motion.div
                    className="flex flex-col items-center justify-center space-y-4 text-center mb-12"
                    initial={{ opacity: 0, y: 20 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    transition={{ duration: 0.5 }}
                >
                    <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">Resources to help you grow</h2>
                    <p className="max-w-[700px] text-muted-foreground md:text-xl">
                        Tips, tricks, and guides on how to maximize your digital presence.
                    </p>
                </motion.div>
                <motion.div
                    className="grid gap-6 md:grid-cols-2 lg:grid-cols-3"
                    initial="initial"
                    whileInView="whileInView"
                    viewport={{ once: true }}
                    variants={staggerContainer}
                >
                    {[1, 2, 3].map((i) => (
                        <motion.div key={i} variants={fadeInUp}>
                            <Card className="bg-background hover:shadow-lg transition-all duration-300 border-border/50 h-full flex flex-col">
                                <div className="aspect-video w-full bg-muted/50 rounded-t-lg object-cover flex items-center justify-center text-muted-foreground">
                                    Article Image
                                </div>
                                <CardHeader>
                                    <div className="text-xs font-semibold text-primary uppercase tracking-wider mb-2">Marketing</div>
                                    <CardTitle className="text-lg leading-tight group-hover:text-primary transition-colors">10 Ways to Increase Click-Through Rates</CardTitle>
                                </CardHeader>
                                <CardContent className="flex-1">
                                    <p className="text-sm text-muted-foreground">
                                        Learn the secrets behind high-converting links and how you can apply them to your campaigns.
                                    </p>
                                </CardContent>
                                <div className="p-6 pt-0 mt-auto">
                                    <Button variant="link" className="p-0 h-auto font-semibold">Read Article <ArrowRight className="w-4 h-4 ml-1" /></Button>
                                </div>
                            </Card>
                        </motion.div>
                    ))}
                </motion.div>
            </div>
        </section>
    )
}

export const FooterSection = () => {
    return (
        <footer className="w-full border-t border-border/40 bg-background pt-16 pb-8">
            <div className="container mx-auto px-4 md:px-6">
                <div className="grid grid-cols-2 gap-8 md:grid-cols-4 lg:grid-cols-5 mb-12">
                    <div className="col-span-2 lg:col-span-2">
                        <div className="flex items-center gap-2 font-bold text-xl mb-4">
                            <div className="bg-primary text-primary-foreground flex size-8 items-center justify-center rounded-md">
                                <Zap className="size-5" />
                            </div>
                            ZapLink
                        </div>
                        <p className="text-muted-foreground text-sm max-w-xs mb-6 leading-relaxed">
                            The advanced URL shortener for modern marketing teams. Track, analyze, and optimize your links.
                        </p>
                        <div className="flex gap-4">
                            {[Twitter, Facebook, Instagram, Linkedin].map((Icon, i) => (
                                <Button key={i} variant="ghost" size="icon" className="h-10 w-10 rounded-full hover:bg-primary/10 hover:text-primary">
                                    <Icon className="h-5 w-5" />
                                </Button>
                            ))}
                        </div>
                    </div>
                    <div className="space-y-4">
                        <h4 className="text-sm font-semibold tracking-wider uppercase text-foreground/80">Product</h4>
                        <ul className="space-y-3 text-sm text-muted-foreground">
                            <li><Link href="#features" className="hover:text-primary transition-colors">Features</Link></li>
                            <li><Link href="#pricing" className="hover:text-primary transition-colors">Pricing</Link></li>
                            <li><Link href="#" className="hover:text-primary transition-colors">Analytics</Link></li>
                            <li><Link href="#" className="hover:text-primary transition-colors">QR Codes</Link></li>
                        </ul>
                    </div>
                    <div className="space-y-4">
                        <h4 className="text-sm font-semibold tracking-wider uppercase text-foreground/80">Resources</h4>
                        <ul className="space-y-3 text-sm text-muted-foreground">
                            <li><Link href="#resources" className="hover:text-primary transition-colors">Blog</Link></li>
                            <li><Link href="#" className="hover:text-primary transition-colors">Documentation</Link></li>
                            <li><Link href="#" className="hover:text-primary transition-colors">Guides</Link></li>
                            <li><Link href="#" className="hover:text-primary transition-colors">Support</Link></li>
                        </ul>
                    </div>
                    <div className="space-y-4">
                        <h4 className="text-sm font-semibold tracking-wider uppercase text-foreground/80">Company</h4>
                        <ul className="space-y-3 text-sm text-muted-foreground">
                            <li><Link href="#" className="hover:text-primary transition-colors">About</Link></li>
                            <li><Link href="#" className="hover:text-primary transition-colors">Careers</Link></li>
                            <li><Link href="#" className="hover:text-primary transition-colors">Legal</Link></li>
                            <li><Link href="#" className="hover:text-primary transition-colors">Contact</Link></li>
                        </ul>
                    </div>
                </div>
                <div className="border-t border-border/40 pt-8 flex flex-col md:flex-row justify-between items-center gap-4 text-sm text-muted-foreground">
                    <p>&copy; {new Date().getFullYear()} ZapLink Inc. All rights reserved.</p>
                    <div className="flex gap-6">
                        <Link href="#" className="hover:text-foreground">Privacy Policy</Link>
                        <Link href="#" className="hover:text-foreground">Terms of Service</Link>
                    </div>
                </div>
            </div>
        </footer>
    )
}