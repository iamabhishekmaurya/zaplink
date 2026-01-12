import React from 'react'
import { useFormContext } from 'react-hook-form'
import { FormField, FormItem, FormControl, FormLabel } from '@/components/ui/form'
import { Switch } from '@/components/ui/switch'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Button } from '@/components/ui/button'
import { FormValues } from '../../constants'
import { Settings, Zap, Globe, Shield, Clock } from 'lucide-react'

export const AdvancedTab = () => {
    const form = useFormContext<FormValues>()

    return (
        <div className="space-y-6">
            {/* Dynamic QR Settings */}
            <div className="space-y-4">
                <Label className="text-sm font-semibold flex items-center gap-2">
                    <Zap className="h-4 w-4" />
                    Dynamic QR Settings
                </Label>
                <div className="grid sm:grid-cols-2 gap-4">
                    <FormField
                        control={form.control}
                        name="enableTracking"
                        render={({ field }) => (
                            <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3">
                                <div className="space-y-0.5">
                                    <FormLabel>Enable Tracking</FormLabel>
                                    <p className="text-xs text-muted-foreground">
                                        Track scans, location, and device analytics
                                    </p>
                                </div>
                                <FormControl>
                                    <Switch
                                        checked={field.value}
                                        onCheckedChange={field.onChange}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />

                    <FormField
                        control={form.control}
                        name="trackAnalytics"
                        render={({ field }) => (
                            <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3">
                                <div className="space-y-0.5">
                                    <FormLabel>Analytics</FormLabel>
                                    <p className="text-xs text-muted-foreground">
                                        Track detailed user behavior and conversions
                                    </p>
                                </div>
                                <FormControl>
                                    <Switch
                                        checked={field.value}
                                        onCheckedChange={field.onChange}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                </div>
            </div>

            {/* Advanced Configuration */}
            <div className="space-y-4">
                <Label className="text-sm font-semibold flex items-center gap-2">
                    <Settings className="h-4 w-4" />
                    Advanced Configuration
                </Label>

                <div className="grid sm:grid-cols-2 gap-4">
                    <FormField
                        control={form.control}
                        name="expirationDays"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Expiration (days)</FormLabel>
                                <Select
                                    onValueChange={(val) => field.onChange(parseInt(val))}
                                    value={field.value?.toString()}
                                >
                                    <SelectTrigger className="h-10">
                                        <SelectValue placeholder="Select expiration" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="0">Never</SelectItem>
                                        <SelectItem value="7">7 Days</SelectItem>
                                        <SelectItem value="30">30 Days</SelectItem>
                                        <SelectItem value="90">90 Days</SelectItem>
                                        <SelectItem value="365">1 Year</SelectItem>
                                    </SelectContent>
                                </Select>
                            </FormItem>
                        )}
                    />

                    <FormField
                        control={form.control}
                        name="passwordProtection"
                        render={({ field }) => (
                            <FormItem className="flex flex-row items-center justify-between rounded-lg border p-3">
                                <div className="space-y-0.5">
                                    <FormLabel>Password Protection</FormLabel>
                                    <p className="text-xs text-muted-foreground">
                                        Require password to access QR content
                                    </p>
                                </div>
                                <FormControl>
                                    <Switch
                                        checked={field.value}
                                        onCheckedChange={field.onChange}
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                </div>
            </div>

            {/* API Gateway Configuration */}
            <div className="space-y-4">
                <Label className="text-sm font-semibold flex items-center gap-2">
                    <Globe className="h-4 w-4" />
                    API Gateway Settings
                </Label>

                <div className="grid sm:grid-cols-2 gap-4">
                    <FormField
                        control={form.control}
                        name="apiGateway"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>API Gateway</FormLabel>
                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                    <SelectTrigger className="h-10">
                                        <SelectValue placeholder="Select gateway" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="api-gateway">API Gateway</SelectItem>
                                        <SelectItem value="core-server">Core Server</SelectItem>
                                        <SelectItem value="direct">Direct Connection</SelectItem>
                                    </SelectContent>
                                </Select>
                            </FormItem>
                        )}
                    />

                    <FormField
                        control={form.control}
                        name="customRedirect"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Custom Redirect URL</FormLabel>
                                <FormControl>
                                    <Input
                                        placeholder="https://your-custom-redirect.com"
                                        {...field}
                                        className="h-10"
                                    />
                                </FormControl>
                            </FormItem>
                        )}
                    />
                </div>
            </div>

            {/* Security Settings */}
            <div className="space-y-4">
                <Label className="text-sm font-semibold flex items-center gap-2">
                    <Shield className="h-4 w-4" />
                    Security Settings
                </Label>

                <div className="grid sm:grid-cols-2 gap-4">
                    <FormField
                        control={form.control}
                        name="domainRestriction"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Domain Restriction</FormLabel>
                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                    <SelectTrigger className="h-10">
                                        <SelectValue placeholder="Select restriction" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="none">No Restriction</SelectItem>
                                        <SelectItem value="allowed">Allowed Domains Only</SelectItem>
                                        <SelectItem value="blocked">Block Specific Domains</SelectItem>
                                    </SelectContent>
                                </Select>
                            </FormItem>
                        )}
                    />

                    <FormField
                        control={form.control}
                        name="scanLimit"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Scan Limit</FormLabel>
                                <Select
                                    onValueChange={(val) => field.onChange(val === 'unlimited' ? 0 : parseInt(val))}
                                    value={field.value === 0 ? 'unlimited' : field.value?.toString()}
                                >
                                    <SelectTrigger className="h-10">
                                        <SelectValue placeholder="Select limit" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="unlimited">Unlimited</SelectItem>
                                        <SelectItem value="100">100 scans</SelectItem>
                                        <SelectItem value="500">500 scans</SelectItem>
                                        <SelectItem value="1000">1000 scans</SelectItem>
                                        <SelectItem value="5000">5000 scans</SelectItem>
                                    </SelectContent>
                                </Select>
                            </FormItem>
                        )}
                    />
                </div>
            </div>

            {/* Action Buttons */}
            <div className="pt-6 border-t">
                <div className="flex gap-4">
                    <Button
                        className="flex-1"
                        onClick={() => {
                            const values = form.getValues()
                            // Create dynamic QR with current settings
                            console.log('Creating dynamic QR with advanced settings:', values)
                        }}
                    >
                        <Clock className="mr-2 h-4 w-4" />
                        Create Dynamic QR
                    </Button>

                    <Button
                        variant="outline"
                        onClick={() => {
                            // Test configuration before deployment
                            console.log('Testing QR configuration...')
                        }}
                    >
                        <Settings className="mr-2 h-4 w-4" />
                        Test Configuration
                    </Button>
                </div>
            </div>
        </div>
    )
}
