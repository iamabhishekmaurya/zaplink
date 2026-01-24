'use client';

import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";

const team = [
    { name: "Alex Rivera", role: "Co-Founder & CEO", avatar: "AR" },
    { name: "Sarah Chen", role: "CTO", avatar: "SC" },
    { name: "Mike Johnson", role: "Head of Product", avatar: "MJ" },
    { name: "Emily Davis", role: "Head of Design", avatar: "ED" },
    { name: "David Kim", role: "Lead Engineer", avatar: "DK" },
    { name: "Lisa Park", role: "Marketing Director", avatar: "LP" },
];

export const TeamGrid = () => {
    return (
        <section className="py-20 bg-muted/30">
            <div className="container mx-auto px-4 md:px-6">
                <div className="text-center mb-12">
                    <h2 className="text-3xl font-bold mb-4">Meet the Team</h2>
                    <p className="text-muted-foreground">The people behind the pixels.</p>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-8">
                    {team.map((member, i) => (
                        <Card key={i} className="bg-card border-border/50 text-center hover:border-primary/50 transition-all">
                            <CardHeader>
                                <div className="mx-auto w-24 h-24 mb-4">
                                    <Avatar className="w-full h-full">
                                        <AvatarImage src={`https://i.pravatar.cc/150?u=${member.name}`} />
                                        <AvatarFallback className="text-xl bg-primary/10 text-primary">{member.avatar}</AvatarFallback>
                                    </Avatar>
                                </div>
                                <CardTitle>{member.name}</CardTitle>
                                <CardDescription>{member.role}</CardDescription>
                            </CardHeader>
                        </Card>
                    ))}
                </div>
            </div>
        </section>
    );
};
