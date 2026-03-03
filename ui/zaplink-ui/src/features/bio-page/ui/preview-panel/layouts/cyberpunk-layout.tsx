"use client";

import { motion, AnimatePresence, Variants } from "framer-motion";
import { Cpu, Zap } from "lucide-react";
import { LayoutProps } from "./types";

export function CyberpunkLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks,
    itemVariants
}: LayoutProps) {
    // Cyberpunk ignores theme colors and forces a neon synthwave aesthetic
    const cyberVariants: Variants = {
        hidden: { opacity: 0, x: -50, skewX: 20 },
        visible: {
            opacity: 1,
            x: 0,
            skewX: 0,
            transition: { type: "spring", stiffness: 100, damping: 10 }
        }
    };

    return (
        <div className="w-full min-h-screen relative z-10 bg-zinc-950 text-cyan-400 font-mono px-4 py-12 flex flex-col items-center overflow-x-hidden selection:bg-pink-500 selection:text-white">

            {/* Scanlines Overlay */}
            <div className="fixed inset-0 pointer-events-none z-50 bg-[linear-gradient(rgba(18,16,16,0)_50%,rgba(0,0,0,0.25)_50%),linear-gradient(90deg,rgba(255,0,0,0.06),rgba(0,255,0,0.02),rgba(0,0,255,0.06))] bg-[length:100%_4px,3px_100%] z-50 mix-blend-overlay" />

            {/* Header */}
            <motion.div variants={cyberVariants} className="w-full max-w-lg mb-12 relative">
                <div className="absolute -inset-1 bg-gradient-to-r from-cyan-500 to-pink-500 rounded border border-cyan-400/50 shadow-[0_0_15px_rgba(6,182,212,0.5)] opacity-50 blur-sm" />
                <div className="relative bg-zinc-950 border-2 border-cyan-400 p-6 shadow-[inset_0_0_20px_rgba(6,182,212,0.2)]">
                    <div className="absolute top-0 right-0 px-2 py-0.5 bg-cyan-400 text-zinc-950 text-xs font-bold tracking-widest uppercase">
                        SYS.REQ.OK
                    </div>

                    <div className="flex items-center gap-6">
                        <div className="w-20 h-20 border-2 border-pink-500 p-1 relative group">
                            <div className="absolute inset-0 bg-pink-500/20 group-hover:bg-pink-500/40 transition-colors" />
                            {page.avatarUrl ? (
                                <img src={page.avatarUrl} className="w-full h-full object-cover filter contrast-125 saturate-150" alt="" />
                            ) : (
                                <div className="w-full h-full bg-zinc-900 flex items-center justify-center"><Cpu className="w-8 h-8 text-pink-500" /></div>
                            )}
                        </div>

                        <div>
                            <h1 className="text-2xl font-black uppercase tracking-widest text-transparent bg-clip-text bg-gradient-to-r from-cyan-400 to-pink-500">
                                {page.title || page.username}
                            </h1>
                            <p className="text-sm text-cyan-200/70 mt-2">
                                {page.bioText || "> STATUS: ONLINE"}
                            </p>
                        </div>
                    </div>
                </div>
            </motion.div>

            {/* Social Matrix */}
            {socialLinks.length > 0 && (
                <motion.div variants={cyberVariants} className="flex flex-wrap justify-center gap-4 mb-12">
                    {socialLinks.map((link: any) => (
                        <a
                            key={link.id}
                            href={link.url}
                            target="_blank"
                            className="w-12 h-12 border border-pink-500/50 bg-pink-500/10 hover:bg-pink-500/30 flex items-center justify-center transition-all hover:shadow-[0_0_15px_rgba(236,72,153,0.6)] hover:scale-110"
                        >
                            {link.iconUrl ? <img src={link.iconUrl} className="w-5 h-5 filter brightness-200" alt="" /> : <span className="text-xs text-pink-400">{link.title[0]}</span>}
                        </a>
                    ))}
                </motion.div>
            )}

            {/* Links Block */}
            <motion.div variants={itemVariants} className="w-full max-w-lg space-y-4">
                <AnimatePresence>
                    {regularLinks.map((link: any, index: number) => {
                        const isEven = index % 2 === 0;
                        const borderColor = isEven ? 'border-cyan-400' : 'border-pink-500';
                        const hoverShadow = isEven ? 'hover:shadow-[0_0_20px_rgba(6,182,212,0.6)]' : 'hover:shadow-[0_0_20px_rgba(236,72,153,0.6)]';
                        const textColor = isEven ? 'text-cyan-400' : 'text-pink-400';
                        const bgHover = isEven ? 'hover:bg-cyan-400/10' : 'hover:bg-pink-500/10';

                        return (
                            <motion.a
                                key={link.id}
                                href={link.url}
                                target="_blank"
                                variants={cyberVariants}
                                className={`block relative border-2 ${borderColor} bg-zinc-950 p-4 transition-all duration-300 ${hoverShadow} ${bgHover} group overflow-hidden`}
                            >
                                <div className={`absolute top-0 right-0 w-4 h-4 border-b-2 border-l-2 ${borderColor} -translate-y-full translate-x-full group-hover:translate-y-0 group-hover:translate-x-0 transition-transform`} />

                                <div className="flex items-center justify-between z-10 relative">
                                    <span className={`font-bold uppercase tracking-widest ${textColor}`}>
                                        <span className="opacity-50 mr-2">{String(index + 1).padStart(2, '0')}</span>
                                        {link.title}
                                    </span>
                                    {link.price && <span className="bg-zinc-800 px-2 py-0.5 text-xs border border-zinc-700">{link.currency}{link.price}</span>}
                                </div>
                                {/* Glitch decorative element */}
                                <div className="absolute top-0 left-0 w-full h-full bg-white opacity-0 group-hover:animate-pulse mix-blend-overlay pointer-events-none" />
                            </motion.a>
                        );
                    })}
                </AnimatePresence>
            </motion.div>

            <div className="mt-16 border-t border-cyan-400/30 pt-4 px-12 border-b">
                <div className="text-[10px] tracking-[0.3em] font-black text-cyan-600 flex items-center justify-center gap-2">
                    <Zap className="w-3 h-3" /> ZAPLINK.OS V.1.0_
                </div>
            </div>
        </div>
    );
}
