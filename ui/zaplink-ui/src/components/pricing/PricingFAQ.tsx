'use client';

import {
    Accordion,
    AccordionContent,
    AccordionItem,
    AccordionTrigger,
} from "@/components/ui/accordion";

const faqs = [
    {
        question: "Can I cancel my subscription at any time?",
        answer: "Yes, you can cancel your subscription at any time. Your plan will remain active until the end of the current billing cycle."
    },
    {
        question: "Do you offer a free trial for paid plans?",
        answer: "We offer a 14-day free trial for both Pro and Business plans. No credit card is required to start your trial."
    },
    {
        question: "What happens to my links if I downgrade?",
        answer: "Your links will continue to work, but you may lose access to advanced analytics and custom domain features associated with your paid plan."
    },
    {
        question: "Can I change my plan later?",
        answer: "Absolutely. You can upgrade or downgrade your plan at any time from your account settings. Changes take effect immediately."
    },
    {
        question: "Do you offer discounts for non-profits?",
        answer: "Yes, we offer special pricing for non-profit organizations and educational institutions. Contact our support team for more details."
    },
];

export default function PricingFAQ() {
    return (
        <section className="py-20 bg-background">
            <div className="max-w-3xl mx-auto px-4">
                <div className="text-center mb-12">
                    <h2 className="text-3xl font-bold mb-4">Frequently Asked <span className="text-primary font-[family-name:var(--font-script)]">Questions</span></h2>
                    <p className="text-muted-foreground">Have questions? We're here to help.</p>
                </div>

                <Accordion type="single" collapsible className="w-full">
                    {faqs.map((faq, index) => (
                        <AccordionItem key={index} value={`item-${index}`} className="border-b-border/50">
                            <AccordionTrigger className="text-left text-lg hover:text-primary transition-colors">
                                {faq.question}
                            </AccordionTrigger>
                            <AccordionContent className="text-muted-foreground text-base">
                                {faq.answer}
                            </AccordionContent>
                        </AccordionItem>
                    ))}
                </Accordion>
            </div>
        </section>
    );
}
