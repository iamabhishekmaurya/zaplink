import React from 'react'
import { FormField } from '@/components/ui/form'
import { useFormContext } from 'react-hook-form'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import { Layers } from 'lucide-react'
import { SmartRoutingRules } from '@/components/smart-routing/SmartRoutingRules'
import { RedirectRuleDto } from '@/lib/types/apiRequestType'

export const SmartRulesTab = () => {
    const form = useFormContext()

    return (
        <FormField
            control={form.control}
            name="rules"
            render={({ field }) => (
                <SmartRoutingRules
                    rules={field.value as RedirectRuleDto[] || []}
                    onChange={field.onChange}
                    variant="plain"
                />
            )}
        />
    )
}
