'use client'

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { CreditCard, TrendingUp, Users, Zap } from 'lucide-react'

export default function BillingPage() {
    return (
        <div className="container mx-auto p-6 space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold">Billing</h1>
                    <p className="text-muted-foreground">
                        Manage your subscription and billing information
                    </p>
                </div>
                <Badge variant="secondary" className="text-sm">
                    Free Plan
                </Badge>
            </div>

            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Current Plan</CardTitle>
                        <CreditCard className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">Free</div>
                        <p className="text-xs text-muted-foreground">
                            100 links per month
                        </p>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Total Links</CardTitle>
                        <TrendingUp className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">24</div>
                        <p className="text-xs text-muted-foreground">
                            +2 from last month
                        </p>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Total Clicks</CardTitle>
                        <Users className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">1,234</div>
                        <p className="text-xs text-muted-foreground">
                            +180 from last month
                        </p>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">QR Codes</CardTitle>
                        <Zap className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">8</div>
                        <p className="text-xs text-muted-foreground">
                            +1 from last month
                        </p>
                    </CardContent>
                </Card>
            </div>

            <div className="grid gap-6 md:grid-cols-2">
                <Card>
                    <CardHeader>
                        <CardTitle>Upgrade Plan</CardTitle>
                        <CardDescription>
                            Get more features and higher limits with our premium plans
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <div className="space-y-2">
                            <div className="flex items-center justify-between p-3 border rounded-lg">
                                <div>
                                    <h3 className="font-medium">Pro Plan</h3>
                                    <p className="text-sm text-muted-foreground">
                                        Unlimited links, advanced analytics
                                    </p>
                                </div>
                                <div className="text-right">
                                    <div className="text-2xl font-bold">$9</div>
                                    <div className="text-sm text-muted-foreground">/month</div>
                                </div>
                            </div>
                            
                            <div className="flex items-center justify-between p-3 border rounded-lg">
                                <div>
                                    <h3 className="font-medium">Enterprise</h3>
                                    <p className="text-sm text-muted-foreground">
                                        Custom solutions for teams
                                    </p>
                                </div>
                                <div className="text-right">
                                    <div className="text-2xl font-bold">$29</div>
                                    <div className="text-sm text-muted-foreground">/month</div>
                                </div>
                            </div>
                        </div>
                        
                        <Button className="w-full">
                            Upgrade to Pro
                        </Button>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader>
                        <CardTitle>Billing History</CardTitle>
                        <CardDescription>
                            View your past invoices and payments
                        </CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-4">
                            <div className="text-center py-8 text-muted-foreground">
                                <CreditCard className="h-12 w-12 mx-auto mb-4 opacity-50" />
                                <p>No billing history yet</p>
                                <p className="text-sm">
                                    Upgrade to a paid plan to see your billing history
                                </p>
                            </div>
                        </div>
                    </CardContent>
                </Card>
            </div>
        </div>
    )
}