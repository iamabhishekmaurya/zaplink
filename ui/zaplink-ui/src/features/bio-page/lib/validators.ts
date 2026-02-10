import { z } from 'zod';

export const linkFormSchema = z.object({
    title: z.string().min(1, "Title is required"),
    url: z.string().url("Must be a valid URL").optional().or(z.literal('')),
    type: z.enum(['LINK', 'SOCIAL', 'PRODUCT', 'EMAIL', 'PHONE', 'EMBED', 'SCHEDULED', 'GATED', 'PAYMENT']),
    isActive: z.boolean().default(true),
    sortOrder: z.number().default(0),
    /* Optional fields based on type */
    price: z.number().optional(),
    currency: z.string().default('USD').optional(),
    scheduleFrom: z.date().nullable().optional(),
    scheduleTo: z.date().nullable().optional(),
    thumbnailUrl: z.string().url("Must be a valid URL").optional().or(z.literal('')),
    iconUrl: z.string().optional(),
    embedCode: z.string().optional(),
    gateType: z.enum(['email', 'password', 'payment']).optional(),
    gateValue: z.string().optional(),
    gateMessage: z.string().optional(),
}).superRefine((data, ctx) => {
    if (data.type === 'LINK' && !data.url) {
        ctx.addIssue({
            code: z.ZodIssueCode.custom,
            message: "URL is required for links",
            path: ["url"]
        });
    }
    if (data.type === 'PRODUCT' && (!data.price || data.price <= 0)) {
        ctx.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Price must be greater than 0",
            path: ["price"]
        });
    }
    if (data.type === 'SCHEDULED' && !data.scheduleFrom) {
        ctx.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Start date is required",
            path: ["scheduleFrom"]
        });
    }
    if (data.type === 'GATED' && !data.gateType) {
        ctx.addIssue({
            code: z.ZodIssueCode.custom,
            message: "Gate type is required",
            path: ["gateType"]
        });
    }
});

export type LinkFormValues = z.infer<typeof linkFormSchema>;
