'use client';

import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';

const jobs = [
    { title: "Senior Frontend Engineer", dept: "Engineering", loc: "Remote (US/EU)", type: "Full-time" },
    { title: "Backend Developer (Go/Node)", dept: "Engineering", loc: "Remote", type: "Full-time" },
    { title: "Product Designer", dept: "Design", loc: "Remote", type: "Full-time" },
    { title: "Growth Marketing Manager", dept: "Marketing", loc: "New York / Remote", type: "Full-time" },
];

export const OpenPositions = () => {
    return (
        <section className="py-20">
            <div className="container mx-auto px-4 md:px-6 max-w-4xl">
                <h2 className="text-3xl font-bold mb-10">Open Positions</h2>
                <div className="space-y-4">
                    {jobs.map((job, i) => (
                        <div key={i} className="flex flex-col sm:flex-row sm:items-center justify-between p-6 rounded-xl border border-border hover:border-primary/50 bg-card transition-colors gap-4">
                            <div>
                                <h3 className="text-lg font-bold">{job.title}</h3>
                                <div className="flex gap-2 mt-2">
                                    <Badge variant="secondary">{job.dept}</Badge>
                                    <span className="text-sm text-muted-foreground flex items-center">{job.loc}</span>
                                </div>
                            </div>
                            <Button variant="outline">Apply Now</Button>
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
};
