'use client';

import { Zap, Heart, Globe, Target } from 'lucide-react';

const values = [
    { title: "Move Fast", desc: "We ship daily. We value speed and iteration over perfection.", icon: Zap },
    { title: "User Obsessed", desc: "We prioritize the user experience above all else.", icon: Heart },
    { title: "Remote First", desc: "Work from anywhere. We trust you to manage your time.", icon: Globe },
    { title: "Impact Driven", desc: "We measure results, not hours in a chair.", icon: Target },
];

export const ValuesSection = () => {
    return (
        <section className="py-20 bg-muted/30">
            <div className="container mx-auto px-4 md:px-6">
                <h2 className="text-3xl font-bold text-center mb-16">Our Values</h2>
                <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
                    {values.map((v, i) => (
                        <div key={i} className="text-center p-6 rounded-2xl bg-background border border-border">
                            <div className="w-12 h-12 mx-auto rounded-xl bg-primary/10 flex items-center justify-center mb-4 text-primary">
                                <v.icon className="w-6 h-6" />
                            </div>
                            <h3 className="font-bold mb-2">{v.title}</h3>
                            <p className="text-sm text-muted-foreground">{v.desc}</p>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
};
