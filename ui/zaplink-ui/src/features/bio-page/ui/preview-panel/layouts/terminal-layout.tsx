"use client";

import { motion } from "framer-motion";
import { LayoutProps } from "./types";

export function TerminalLayout({
    page,
    previewMode,
    socialLinks,
    regularLinks
}: LayoutProps) {

    // Retro Terminal Aesthetic - DOS style
    return (
        <div className="w-full min-h-screen relative z-10 bg-black text-[#55FF55] font-mono p-4 sm:p-8 select-none">

            <div className="w-full max-w-3xl mx-auto border-[4px] border-zinc-800 p-6 min-h-[80vh] bg-black shadow-[inset_0_0_30px_rgba(0,0,0,1)] relative">

                <div className="mb-8">
                    <p className="mb-1">Zaplink DOS Version 1.00</p>
                    <p className="mb-1">(C)Copyright Zaplink Corp 2024</p>
                    <br />
                    <p className="mb-1">{`C:\\> RUN PROFILE.EXE /USER=${page.username}`}</p>
                    <p className="animate-pulse">Loading profile data...</p>
                </div>

                <div className="border border-[#55FF55] p-4 mb-8">
                    <div className="flex flex-col sm:flex-row gap-6">
                        {page.avatarUrl ? (
                            <div className="w-24 h-24 border border-[#55FF55] p-1">
                                <img src={page.avatarUrl} alt="" className="w-full h-full object-cover grayscale contrast-200 brightness-50 sepia-[1] hue-rotate-[70deg] saturate-200" />
                            </div>
                        ) : (
                            <div className="w-24 h-24 border border-[#55FF55] flex items-center justify-center">
                                <span className="text-4xl text-[#55FF55]">?</span>
                            </div>
                        )}

                        <div>
                            <h1 className="text-xl font-bold uppercase mb-2">== {page.title || page.username} ==</h1>
                            <p className="text-sm opacity-80 max-w-md">{page.bioText || "NO DIR INFO FOUND"}</p>
                        </div>
                    </div>
                </div>

                <div className="mb-8">
                    <p className="mb-2 uppercase border-b border-[#55FF55] inline-block">Directory of Socials:</p>
                    <div className="grid grid-cols-2 sm:grid-cols-3 gap-2 mt-2">
                        {socialLinks.map((link: any, idx: number) => (
                            <a key={link.id} href={link.url} target="_blank" className="hover:bg-[#55FF55] hover:text-black px-1 transition-none truncate">
                                [{String(idx).padStart(2, '0')}] {link.title.toUpperCase()}
                            </a>
                        ))}
                        {socialLinks.length === 0 && <span className="opacity-50">Empty</span>}
                    </div>
                </div>

                <div>
                    <p className="mb-4 uppercase border-b border-[#55FF55] inline-block">Executable Links:</p>
                    <div className="space-y-2">
                        {regularLinks.map((link: any, idx: number) => (
                            <a
                                key={link.id}
                                href={link.url}
                                target="_blank"
                                className="block border border-[#55FF55] p-2 hover:bg-[#55FF55] hover:text-black transition-none group flex justify-between items-center"
                            >
                                <span className="uppercase font-bold truncate">CMD&gt; {link.title}</span>
                                <span className="opacity-0 group-hover:opacity-100">&lt;EXEC&gt;</span>
                            </a>
                        ))}
                        {regularLinks.length === 0 && previewMode && <p className="opacity-50">NO EXECUTABLES FOUND IN DIR</p>}
                    </div>
                </div>

                <div className="mt-12 flex items-center gap-2">
                    <span>{`C:\\ZAPLINK\\${page.username}>`}</span>
                    <span className="w-2.5 h-5 bg-[#55FF55] animate-pulse inline-block" />
                </div>
            </div>
        </div>
    );
}
