import React, { useState } from 'react';
import { RedirectRuleDto } from '@/lib/types/apiRequestType';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Trash2, Plus, ArrowRight, Layers, Smartphone, Globe, Monitor } from 'lucide-react';
import { Badge } from '@/components/ui/badge';

interface SmartRoutingRulesProps {
    rules: RedirectRuleDto[];
    onChange: (rules: RedirectRuleDto[]) => void;
    variant?: 'card' | 'plain';
}

const DIMENSIONS = [
    { value: 'DEVICE_TYPE', label: 'Device Type', icon: Smartphone },
    { value: 'OS', label: 'Operating System', icon: Monitor },
    { value: 'COUNTRY', label: 'Country', icon: Globe },
];

export const SmartRoutingRules: React.FC<SmartRoutingRulesProps> = ({ rules = [], onChange, variant = 'card' }) => {
    const [newRule, setNewRule] = useState<Partial<RedirectRuleDto>>({
        dimension: 'DEVICE_TYPE',
        value: '',
        destinationUrl: '',
        priority: 1
    });

    const handleAddRule = () => {
        if (newRule.dimension && newRule.value && newRule.destinationUrl) {
            const ruleToAdd = {
                ...newRule,
                priority: rules.length + 1 // Simple auto-increment priority
            } as RedirectRuleDto;

            onChange([...rules, ruleToAdd]);
            setNewRule({
                dimension: 'DEVICE_TYPE',
                value: '',
                destinationUrl: '',
                priority: rules.length + 2
            });
        }
    };

    const handleRemoveRule = (index: number) => {
        const updatedRules = rules.filter((_, i) => i !== index);
        // Re-calculate priorities
        const reorderedRules = updatedRules.map((r, i) => ({ ...r, priority: i + 1 }));
        onChange(reorderedRules);
    };

    const Content = (
        <div className="space-y-6">
            {/* Add New Rule Form */}
            <div className="grid grid-cols-1 md:grid-cols-12 gap-4 p-4 bg-secondary/20 rounded-lg border border-border/50">
                <div className="md:col-span-3 flex flex-col gap-2">
                    <label className="text-sm font-medium">When</label>
                    <Select
                        value={newRule.dimension}
                        onValueChange={(val) => setNewRule({ ...newRule, dimension: val as any })}
                    >
                        <SelectTrigger className="w-full">
                            <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                            {DIMENSIONS.map(d => (
                                <SelectItem key={d.value} value={d.value}>
                                    <div className="flex items-center gap-2">
                                        <d.icon className="h-4 w-4" />
                                        {d.label}
                                    </div>
                                </SelectItem>
                            ))}
                        </SelectContent>
                    </Select>
                </div>

                <div className="md:col-span-3 flex flex-col gap-2">
                    <label className="text-sm font-medium">Is / Equals</label>
                    <Input
                        placeholder={newRule.dimension === 'COUNTRY' ? 'e.g. US, IN' : 'e.g. iOS, Android'}
                        value={newRule.value}
                        onChange={(e) => setNewRule({ ...newRule, value: e.target.value })}
                    />
                </div>

                <div className="md:col-span-5 flex flex-col gap-2">
                    <label className="text-sm font-medium">Redirect To</label>
                    <Input
                        placeholder="https://example.com/target"
                        value={newRule.destinationUrl}
                        onChange={(e) => setNewRule({ ...newRule, destinationUrl: e.target.value })}
                    />
                </div>

                <div className="md:col-span-1 flex flex-col gap-2">
                    <label className="text-sm font-medium invisible">Add</label>
                    <Button
                        type="button"
                        onClick={handleAddRule}
                        disabled={!newRule.value || !newRule.destinationUrl}
                        className="w-full"
                    >
                        <Plus className="h-4 w-4" />
                    </Button>
                </div>
            </div>

            {/* Rules List */}
            <div className="space-y-3">
                {rules.length === 0 && (
                    <div className="text-center py-8 text-muted-foreground text-sm border border-dashed rounded-lg">
                        No rules added yet. All users will go to the main destination URL.
                    </div>
                )}

                {rules.map((rule, index) => (
                    <div key={index} className="flex flex-col md:flex-row items-center gap-4 p-3 bg-card border rounded-lg group hover:border-primary/30 transition-colors">
                        <div className="flex items-center gap-2 min-w-[120px]">
                            <Badge variant="outline" className="font-mono">
                                #{index + 1}
                            </Badge>
                            <span className="text-sm font-medium">
                                {DIMENSIONS.find(d => d.value === rule.dimension)?.label || rule.dimension}
                            </span>
                        </div>

                        <div className="flex items-center gap-2 flex-1">
                            <Badge variant="secondary">{rule.value}</Badge>
                            <ArrowRight className="h-4 w-4 text-muted-foreground" />
                            <span className="text-sm text-primary truncate max-w-[200px] md:max-w-xs" title={rule.destinationUrl}>
                                {rule.destinationUrl}
                            </span>
                        </div>

                        <Button
                            type="button"
                            variant="ghost"
                            size="icon"
                            onClick={() => handleRemoveRule(index)}
                            className="opacity-0 group-hover:opacity-100 transition-opacity"
                        >
                            <Trash2 className="h-4 w-4 text-destructive" />
                        </Button>
                    </div>
                ))}
            </div>
        </div>
    );

    if (variant === 'plain') {
        return (
            <div className="space-y-4">
                <div className="flex items-center gap-2 mb-4">
                    <Layers className="h-5 w-5 text-primary" />
                    <div>
                        <h3 className="text-base font-semibold">Smart Routing Rules</h3>
                        <p className="text-xs text-muted-foreground">Redirect users based on device, OS, or location.</p>
                    </div>
                </div>
                {Content}
            </div>
        )
    }

    return (
        <Card className="border-border/50 bg-background/50 backdrop-blur-sm shadow-sm">
            <CardHeader>
                <CardTitle className="flex items-center gap-2">
                    <Layers className="h-5 w-5 text-primary" /> Smart Routing Rules
                </CardTitle>
                <CardDescription>
                    Redirect users to different destinations based on their device, OS, or location.
                </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
                {Content}
            </CardContent>
        </Card>
    );
};
