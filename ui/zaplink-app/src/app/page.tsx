'use client';

import { useState } from 'react';
import Link from 'next/link';
import MainLayout from '@/components/layout/MainLayout';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent } from '@/components/ui/card';
import {
  Zap,
  BarChart3,
  ShieldCheck,
  Globe,
  Copy,
  ExternalLink,
  ArrowRight,
  Link2,
  Check,
  Sparkles,
  Rocket,
  Star,
  Users,
  TrendingUp,
  Lock
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { toast } from 'sonner';
import { useLinks } from '@/hooks/useLinks';

export default function Home() {
  const [url, setUrl] = useState('');
  const [shortenedUrl, setShortenedUrl] = useState<string | null>(null);
  const [isCopied, setIsCopied] = useState(false);
  const { shortenUrl, isLoading: isShortening } = useLinks();

  const handleShorten = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!url) return;

    const result = await shortenUrl(url);
    if (result) {
      setShortenedUrl(result.shortUrl);
      setUrl('');
    }
  };

  const copyToClipboard = () => {
    if (shortenedUrl) {
      navigator.clipboard.writeText(shortenedUrl);
      setIsCopied(true);
      toast.info('Copied to clipboard');
      setTimeout(() => setIsCopied(false), 2000);
    }
  };

  return (
    <MainLayout>
      {/* Hero Section - Stunning Gradient Background */}
      <section className="relative min-h-screen flex items-center justify-center overflow-hidden">
        {/* Animated Gradient Background */}
        <div className="absolute inset-0 gradient-primary opacity-90" />
        <div className="absolute inset-0 gradient-secondary opacity-30 animate-pulse" />

        {/* Floating Elements */}
        <motion.div
          className="absolute top-20 left-10 w-20 h-20 bg-white/10 rounded-full blur-xl float-animation"
          initial={{ opacity: 0 }}
          animate={{ opacity: 0.6 }}
          transition={{ delay: 0.5 }}
        />
        <motion.div
          className="absolute top-40 right-20 w-32 h-32 bg-accent/10 rounded-full blur-2xl float-animation"
          initial={{ opacity: 0 }}
          animate={{ opacity: 0.4 }}
          transition={{ delay: 0.7 }}
        />
        <motion.div
          className="absolute bottom-20 left-1/4 w-24 h-24 bg-white/5 rounded-full blur-xl float-animation"
          initial={{ opacity: 0 }}
          animate={{ opacity: 0.5 }}
          transition={{ delay: 0.9 }}
        />

        <div className="container mx-auto px-4 relative z-10">
          <div className="flex flex-col items-center max-w-5xl mx-auto text-center">
            {/* Hero Badge */}
            <motion.div
              initial={{ opacity: 0, scale: 0.8 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ duration: 0.5 }}
              className="mb-8"
            >
              <div className="inline-flex items-center gap-2 px-4 py-2 bg-white/10 backdrop-blur-md border border-white/20 rounded-full text-white text-sm font-medium">
                <Sparkles className="w-4 h-4" />
                <span>Trusted by 100,000+ users worldwide</span>
                <Star className="w-4 h-4 text-yellow-300" />
              </div>
            </motion.div>

            {/* Stunning Hero Heading */}
            <motion.h1
              initial={{ opacity: 0, y: 30 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.8, delay: 0.2 }}
              className="text-5xl md:text-8xl font-bold font-display text-white leading-[1.1] mb-8"
            >
              Transform Your
              <br />
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-yellow-300 via-pink-300 to-purple-300">
                Digital Links
              </span>
            </motion.h1>

            {/* Hero Subtitle */}
            <motion.p
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.8, delay: 0.4 }}
              className="text-xl md:text-2xl text-white/80 max-w-3xl mx-auto mb-12 leading-relaxed"
            >
              Create powerful, trackable short links that captivate your audience
              and boost your brand presence with cutting-edge analytics.
            </motion.p>

            {/* Central Action Card - Glassmorphism */}
            <motion.div
              initial={{ opacity: 0, y: 40, scale: 0.95 }}
              animate={{ opacity: 1, y: 0, scale: 1 }}
              transition={{ duration: 0.8, delay: 0.6 }}
              className="w-full max-w-4xl glass-card rounded-3xl p-8 md:p-12 shadow-2xl"
            >
              {/* Tab Navigation */}
              <div className="flex bg-white/5 backdrop-blur-sm p-1 rounded-2xl mb-8">
                <div className="flex-1 py-3 px-6 text-center font-semibold font-display bg-white/20 rounded-xl text-white border border-white/10">
                  <div className="flex items-center justify-center gap-2">
                    <Link2 className="w-4 h-4" />
                    Short Link
                  </div>
                </div>
                <div className="flex-1 py-3 px-6 text-center font-semibold font-display text-white/60 cursor-not-allowed">
                  <div className="flex items-center justify-center gap-2">
                    <Zap className="w-4 h-4" />
                    QR Code
                  </div>
                </div>
              </div>

              {/* Main Form */}
              <form onSubmit={handleShorten} className="space-y-6">
                <div className="space-y-3">
                  <label className="text-white/80 text-sm font-bold uppercase tracking-wider ml-2">
                    Enter Your Long URL
                  </label>
                  <div className="relative group">
                    <div className="absolute inset-y-0 left-0 pl-6 flex items-center pointer-events-none">
                      <Link2 className="h-6 w-6 text-white/60 group-focus-within:text-white transition-colors" />
                    </div>
                    <Input
                      placeholder="https://your-long-url-here.com/very/long/path"
                      className="bg-white/10 backdrop-blur-md border border-white/20 h-16 pl-16 pr-6 text-lg text-white placeholder:text-white/40 rounded-2xl focus-visible:ring-white/30 focus:border-white/40 transition-all font-medium"
                      value={url}
                      onChange={(e) => setUrl(e.target.value)}
                    />
                  </div>
                </div>

                <Button
                  type="submit"
                  size="lg"
                  className="h-16 rounded-2xl text-lg font-semibold font-display gradient-primary hover:shadow-2xl hover:scale-105 transition-all duration-300 w-full md:w-auto md:px-16 self-center md:self-end text-white border-0"
                  disabled={isShortening}
                >
                  {isShortening ? (
                    <div className="flex items-center gap-3">
                      <div className="h-5 w-5 border-2 border-white/30 border-t-white animate-spin rounded-full" />
                      <span>Creating Magic...</span>
                    </div>
                  ) : (
                    <>
                      <Rocket className="w-5 h-5 mr-2" />
                      Create Short Link
                      <ArrowRight className="ml-2 w-5 h-5" />
                    </>
                  )}
                </Button>
              </form>

              {/* Success Result */}
              <AnimatePresence>
                {shortenedUrl && (
                  <motion.div
                    initial={{ opacity: 0, height: 0, y: 20 }}
                    animate={{ opacity: 1, height: 'auto', y: 0 }}
                    exit={{ opacity: 0, height: 0, y: -20 }}
                    transition={{ duration: 0.5 }}
                    className="border-t border-white/20 pt-8 mt-8"
                  >
                    <div className="bg-gradient-to-r from-green-500/20 to-emerald-500/20 backdrop-blur-sm p-6 rounded-2xl border border-green-400/30">
                      <div className="flex flex-col md:flex-row items-center justify-between gap-6">
                        <div className="flex flex-col gap-2 text-left">
                          <span className="text-xs font-bold uppercase tracking-wider text-green-300">Your Short Link</span>
                          <span className="text-2xl font-bold text-white break-all leading-none">{shortenedUrl}</span>
                        </div>
                        <div className="flex gap-3 w-full md:w-auto">
                          <Button
                            variant="outline"
                            onClick={copyToClipboard}
                            className="flex-grow md:flex-none h-14 px-8 gap-2 rounded-xl bg-white/10 border-white/20 text-white hover:bg-white/20 transition-all font-bold"
                          >
                            {isCopied ? <Check className="h-5 w-5 text-green-400" /> : <Copy className="h-5 w-5" />}
                            {isCopied ? 'COPIED!' : 'COPY'}
                          </Button>
                          <Button
                            size="icon"
                            className="h-14 w-14 rounded-xl bg-gradient-to-r from-green-500 to-emerald-500 text-white hover:shadow-lg transition-all"
                          >
                            <ExternalLink className="h-5 w-5" />
                          </Button>
                        </div>
                      </div>
                    </div>
                  </motion.div>
                )}
              </AnimatePresence>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Stats Section - Stunning Gradient Cards */}
      <section className="py-24 bg-gradient-to-br from-background to-muted/20 relative overflow-hidden">
        {/* Background Pattern */}
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_30%_20%,_rgba(102,126,234,0.1)_0%,_transparent_50%)]" />

        <div className="container mx-auto px-4 relative z-10">
          <div className="text-center mb-16">
            <motion.h2
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
              className="text-4xl md:text-5xl font-bold mb-4"
            >
              <span className="text-gradient">Trusted by Leaders</span>
            </motion.h2>
            <motion.p
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.1 }}
              className="text-xl text-muted-foreground"
            >
              Join thousands of businesses that rely on Zaplink
            </motion.p>
          </div>

          <div className="grid grid-cols-2 lg:grid-cols-4 gap-8">
            {[
              { label: 'Links Created', value: '2.5M+', icon: Link2, gradient: 'from-blue-500 to-cyan-500' },
              { label: 'Clicks Tracked', value: '1B+', icon: TrendingUp, gradient: 'from-purple-500 to-pink-500' },
              { label: 'Active Users', value: '500K+', icon: Users, gradient: 'from-green-500 to-emerald-500' },
              { label: 'Countries', value: '195+', icon: Globe, gradient: 'from-orange-500 to-red-500' },
            ].map((stat, i) => (
              <motion.div
                key={i}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: i * 0.1 }}
                className="text-center"
              >
                <div className={`inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-r ${stat.gradient} mb-4 shadow-lg`}>
                  <stat.icon className="w-8 h-8 text-white" />
                </div>
                <div className="text-4xl md:text-5xl font-bold text-gradient mb-2">{stat.value}</div>
                <div className="text-sm font-medium text-muted-foreground uppercase tracking-wider">{stat.label}</div>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* Features Grid - Modern Glass Cards */}
      <section className="py-24 bg-gradient-to-br from-muted/20 to-background relative">
        <div className="container mx-auto px-4">
          <div className="text-center max-w-3xl mx-auto mb-20">
            <motion.h2
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6 }}
              className="text-4xl md:text-6xl font-bold mb-6"
            >
              Built for <span className="text-gradient">Modern Teams</span>
            </motion.h2>
            <motion.p
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.6, delay: 0.1 }}
              className="text-xl text-muted-foreground leading-relaxed"
            >
              Enterprise-grade features with the simplicity your team deserves.
              Scale from startup to enterprise without missing a beat.
            </motion.p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            {[
              {
                title: 'Lightning Fast',
                desc: 'Global CDN with 100+ edge locations ensures your links redirect instantly, anywhere in the world.',
                icon: Zap,
                gradient: 'from-yellow-400 to-orange-500',
                features: ['< 50ms redirect time', 'Global CDN', 'Auto-scaling']
              },
              {
                title: 'Advanced Analytics',
                desc: 'Deep insights into your audience with real-time analytics, geographic data, and device tracking.',
                icon: BarChart3,
                gradient: 'from-blue-500 to-purple-500',
                features: ['Real-time data', 'Geographic insights', 'Device tracking']
              },
              {
                title: 'Enterprise Security',
                desc: 'Bank-level security with automatic malware scanning, phishing protection, and SSL encryption.',
                icon: ShieldCheck,
                gradient: 'from-green-500 to-emerald-500',
                features: ['SSL encryption', 'Malware scanning', 'GDPR compliant']
              },
              {
                title: 'Custom Domains',
                desc: 'Strengthen your brand with custom domains and branded short links that build trust.',
                icon: Globe,
                gradient: 'from-purple-500 to-pink-500',
                features: ['Custom domains', 'Branded links', 'SSL included']
              },
              {
                title: 'Team Collaboration',
                desc: 'Work together seamlessly with team management, role-based permissions, and shared workspaces.',
                icon: Users,
                gradient: 'from-cyan-500 to-blue-500',
                features: ['Team management', 'Role permissions', 'Shared workspaces']
              },
              {
                title: 'API First',
                desc: 'Complete REST API with webhooks, SDKs, and comprehensive documentation for developers.',
                icon: Lock,
                gradient: 'from-red-500 to-pink-500',
                features: ['REST API', 'Webhooks', 'SDKs included']
              }
            ].map((feature, i) => (
              <motion.div
                key={i}
                initial={{ opacity: 0, y: 40 }}
                whileInView={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6, delay: i * 0.1 }}
                whileHover={{ y: -8 }}
                className="group"
              >
                <Card className="h-full glass-card border-0 shadow-2xl hover:shadow-3xl transition-all duration-300 rounded-3xl overflow-hidden">
                  <CardContent className="pt-8 pb-2">
                    <div className={`inline-flex items-center justify-center w-16 h-16 rounded-2xl bg-gradient-to-r ${feature.gradient} mb-6 shadow-lg group-hover:scale-110 transition-transform duration-300`}>
                      <feature.icon className="w-8 h-8 text-white" />
                    </div>

                    <h3 className="text-2xl font-bold mb-4 text-gradient">{feature.title}</h3>
                    <p className="text-muted-foreground mb-6 leading-relaxed">{feature.desc}</p>

                    <div className="space-y-2">
                      {feature.features.map((item, idx) => (
                        <div key={idx} className="flex items-center gap-2 text-sm text-muted-foreground">
                          <div className="w-1.5 h-1.5 rounded-full bg-primary" />
                          {item}
                        </div>
                      ))}
                    </div>

                    <Button variant="ghost" className="mt-6 px-4 py-2 h-auto font-bold text-primary hover:bg-primary/15 dark:hover:bg-primary/20 hover:text-primary dark:hover:text-primary group-hover:translate-x-1 transition-all rounded-full -ml-4 border border-transparent hover:border-primary/30">
                      Learn more <ArrowRight className="ml-2 w-4 h-4" />
                    </Button>
                  </CardContent>
                </Card>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA Section - Stunning Gradient */}
      <section className="py-32 relative overflow-hidden">
        <div className="absolute inset-0 gradient-primary opacity-95" />
        <div className="absolute inset-0 gradient-secondary opacity-30 animate-pulse" />

        {/* Floating Elements */}
        <motion.div
          className="absolute top-10 right-10 w-32 h-32 bg-white/10 rounded-full blur-2xl float-animation"
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 0.6 }}
        />
        <motion.div
          className="absolute bottom-10 left-10 w-24 h-24 bg-accent/10 rounded-full blur-xl float-animation"
          initial={{ opacity: 0 }}
          whileInView={{ opacity: 0.4 }}
          style={{ animationDelay: '2s' }}
        />

        <div className="container mx-auto px-4 relative z-10">
          <div className="max-w-4xl mx-auto text-center">
            <motion.div
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.8 }}
            >
              <h2 className="text-4xl md:text-7xl font-bold text-white mb-8 leading-tight">
                Ready to Transform
                <br />
                <span className="text-transparent bg-clip-text bg-gradient-to-r from-yellow-300 via-pink-300 to-purple-300">
                  Your Links?
                </span>
              </h2>

              <p className="text-xl md:text-2xl text-white/80 mb-12 leading-relaxed max-w-2xl mx-auto">
                Join 500,000+ users who are already creating powerful,
                trackable links that drive real results.
              </p>

              <div className="flex flex-col sm:flex-row gap-6 justify-center items-center">
                <Link href="/signup">
                  <Button size="lg" className="h-16 px-12 text-lg font-bold bg-white text-primary hover:bg-white/90 hover:scale-105 transition-all duration-300 rounded-2xl shadow-2xl">
                    <Rocket className="w-5 h-5 mr-2" />
                    Start Free Today
                  </Button>
                </Link>
                <Link href="/pricing">
                  <Button size="lg" variant="outline" className="h-16 px-12 text-lg font-bold bg-transparent border-white/30 text-white hover:bg-white/10 hover:border-white/50 transition-all duration-300 rounded-2xl">
                    View Pricing Plans
                  </Button>
                </Link>
              </div>

              <div className="mt-12 flex items-center justify-center gap-8 text-white/60">
                <div className="flex items-center gap-2">
                  <Check className="w-5 h-5 text-green-400" />
                  <span>No credit card required</span>
                </div>
                <div className="flex items-center gap-2">
                  <Check className="w-5 h-5 text-green-400" />
                  <span>14-day free trial</span>
                </div>
                <div className="flex items-center gap-2">
                  <Check className="w-5 h-5 text-green-400" />
                  <span>Cancel anytime</span>
                </div>
              </div>
            </motion.div>
          </div>
        </div>
      </section>
    </MainLayout>
  );
}
