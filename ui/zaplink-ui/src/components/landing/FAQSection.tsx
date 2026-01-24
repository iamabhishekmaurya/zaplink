'use client';

import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { motion } from "motion/react";

const faqs = [
    {
        question: "How does Zaplink improve my link performance?",
        answer: "Zaplink provides AI-powered analytics that help you understand your audience better, optimize link placement, and track conversions in real-time."
    },
    {
        question: "Is my data secure?",
        answer: "Absolutely. We use bank-grade encryption (AES-256) for all data at rest and in transit. Your links are protected with optional password protection."
    },
    {
        question: "Can I integrate Zaplink with my CRM?",
        answer: "Yes! Zaplink integrates seamlessly with popular CRMs like Salesforce, HubSpot, and more."
    },
    {
        question: "Is there a free trial?",
        answer: "Yes, we offer a generous free tier that includes up to 50 short links per month, basic analytics, and 5 QR codes."
    }
];

export const FAQSection = () => {
    return (
        <section className="py-32 bg-muted/30">
            <div className="container mx-auto px-4 md:px-6 max-w-2xl">
                <motion.div
                    className="text-center mb-10"
                    initial={{ opacity: 0, y: 30 }}
                    whileInView={{ opacity: 1, y: 0 }}
                    viewport={{ once: true }}
                    transition={{ duration: 0.6 }}
                >
                    <span className="text-sm font-medium text-primary mb-2 block">FAQ</span>
                    <h2 className="text-3xl font-bold text-foreground">
                        Frequently Asked Questions
                    </h2>
                    <p className="text-muted-foreground mt-2">Everything you need to know before getting started.</p>
                </motion.div>

                <Accordion type="single" collapsible className="space-y-3">
                    {faqs.map((faq, i) => (
                        <AccordionItem
                            key={i}
                            value={`item-${i}`}
                            className="bg-card border border-border rounded-xl px-5"
                        >
                            <AccordionTrigger className="text-left font-medium text-foreground hover:no-underline py-4">
                                {faq.question}
                            </AccordionTrigger>
                            <AccordionContent className="text-muted-foreground pb-4 text-sm">
                                {faq.answer}
                            </AccordionContent>
                        </AccordionItem>
                    ))}
                </Accordion>
            </div>
        </section>
    );
};
