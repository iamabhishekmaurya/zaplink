'use client';

import { useState } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '@/store';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Switch } from '@/components/ui/switch';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Separator } from '@/components/ui/separator';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import {
    User,
    Bell,
    Shield,
    Palette,
    Mail,
    Lock,
    Smartphone,
    Globe,
    Moon,
    Sun,
    Monitor,
    AlertTriangle,
    Save,
    RotateCcw
} from 'lucide-react';
import { motion, AnimatePresence } from 'framer-motion';
import { useTheme } from 'next-themes';
import { useEffect } from 'react';

export default function SettingsPage() {
    const { user } = useSelector((state: RootState) => state.auth);
    const { theme, setTheme } = useTheme();

    // Profile Settings
    const [profileSettings, setProfileSettings] = useState({
        username: user?.username || '',
        email: user?.email || '',
        firstName: user?.firstName || '',
        lastName: user?.lastName || '',
        phoneNumber: user?.phoneNumber || '',
    });

    // Notification Settings
    const [notificationSettings, setNotificationSettings] = useState({
        emailNotifications: true,
        pushNotifications: false,
        marketingEmails: false,
        securityAlerts: true,
        linkActivity: true,
        weeklyDigest: false,
    });

    // Security Settings
    const [securitySettings, setSecuritySettings] = useState({
        twoFactorEnabled: false,
        sessionTimeout: true,
        loginAlerts: true,
        passwordChangeRequired: false,
    });

    const [appearanceSettings, setAppearanceSettings] = useState({
        theme: theme || 'system',
        language: 'en',
        dateFormat: 'MM/DD/YYYY',
        timezone: 'UTC',
    });

    // Sync profile settings when user data loads
    useEffect(() => {
        if (user) {
            setProfileSettings({
                username: user.username || '',
                email: user.email || '',
                firstName: user.firstName || '',
                lastName: user.lastName || '',
                phoneNumber: user.phoneNumber || '',
            });
        }
    }, [user]);

    // Sync theme when it changes externally
    useEffect(() => {
        if (theme) {
            setAppearanceSettings(prev => ({ ...prev, theme }));
        }
    }, [theme]);

    const handleProfileChange = (field: string, value: string) => {
        setProfileSettings(prev => ({ ...prev, [field]: value }));
    };

    const handleNotificationChange = (field: string, value: boolean) => {
        setNotificationSettings(prev => ({ ...prev, [field]: value }));
    };

    const handleSecurityChange = (field: string, value: boolean) => {
        setSecuritySettings(prev => ({ ...prev, [field]: value }));
    };

    const handleAppearanceChange = (field: string, value: string) => {
        setAppearanceSettings(prev => ({ ...prev, [field]: value }));
        if (field === 'theme') {
            setTheme(value);
        }
    };

    const handleSaveSettings = () => {
        // TODO: Implement API call to save settings
        console.log('Saving settings:', {
            profile: profileSettings,
            notifications: notificationSettings,
            security: securitySettings,
            appearance: appearanceSettings,
        });
    };

    const containerVariants = {
        hidden: { opacity: 0, y: 20 },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 0.6,
                staggerChildren: 0.1
            }
        }
    };

    const itemVariants = {
        hidden: { opacity: 0, y: 20 },
        visible: { opacity: 1, y: 0 }
    };

    return (
        <motion.div
            variants={containerVariants}
            initial="hidden"
            animate="visible"
            className="container mx-auto px-4 py-8 max-w-4xl"
        >
            <div className="mb-8">
                <h1 className="text-3xl font-bold font-display tracking-tight text-foreground mb-2">Settings</h1>
                <p className="text-muted-foreground font-medium">Manage your account settings and preferences</p>
            </div>

            <Tabs defaultValue="profile" className="space-y-6">
                <TabsList className="grid w-full grid-cols-4 h-11 bg-muted/50 p-1">
                    <TabsTrigger value="profile" className="gap-2 font-bold font-display">
                        <User className="h-4 w-4" />
                        Profile
                    </TabsTrigger>
                    <TabsTrigger value="notifications" className="gap-2 font-bold font-display">
                        <Bell className="h-4 w-4" />
                        Notifications
                    </TabsTrigger>
                    <TabsTrigger value="security" className="gap-2 font-bold font-display">
                        <Shield className="h-4 w-4" />
                        Security
                    </TabsTrigger>
                    <TabsTrigger value="appearance" className="gap-2 font-bold font-display">
                        <Palette className="h-4 w-4" />
                        Appearance
                    </TabsTrigger>
                </TabsList>

                {/* Profile Settings */}
                <TabsContent value="profile" className="outline-none">
                    <AnimatePresence mode="wait">
                        <motion.div
                            key="profile"
                            variants={itemVariants}
                            initial="hidden"
                            animate="visible"
                            exit="hidden"
                        >
                            <Card className="glass-card border-0 shadow-xl">
                                <CardHeader>
                                    <CardTitle className="flex items-center gap-2 font-bold font-display tracking-tight">
                                        <User className="h-5 w-5 text-primary" />
                                        Profile Information
                                    </CardTitle>
                                    <CardDescription className="font-medium">Update your personal information and contact details</CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-4">
                                    <div className="grid md:grid-cols-2 gap-4">
                                        <div>
                                            <Label htmlFor="username">Username</Label>
                                            <Input
                                                id="username"
                                                value={profileSettings.username}
                                                onChange={(e) => handleProfileChange('username', e.target.value)}
                                                className="mt-1"
                                            />
                                        </div>
                                        <div>
                                            <Label htmlFor="email">Email</Label>
                                            <Input
                                                id="email"
                                                type="email"
                                                value={profileSettings.email}
                                                onChange={(e) => handleProfileChange('email', e.target.value)}
                                                className="mt-1"
                                            />
                                        </div>
                                        <div>
                                            <Label htmlFor="firstName">First Name</Label>
                                            <Input
                                                id="firstName"
                                                value={profileSettings.firstName}
                                                onChange={(e) => handleProfileChange('firstName', e.target.value)}
                                                className="mt-1"
                                            />
                                        </div>
                                        <div>
                                            <Label htmlFor="lastName">Last Name</Label>
                                            <Input
                                                id="lastName"
                                                value={profileSettings.lastName}
                                                onChange={(e) => handleProfileChange('lastName', e.target.value)}
                                                className="mt-1"
                                            />
                                        </div>
                                        <div>
                                            <Label htmlFor="phoneNumber">Phone Number</Label>
                                            <Input
                                                id="phoneNumber"
                                                value={profileSettings.phoneNumber}
                                                onChange={(e) => handleProfileChange('phoneNumber', e.target.value)}
                                                className="mt-1"
                                            />
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                    </AnimatePresence>
                </TabsContent>

                {/* Notification Settings */}
                <TabsContent value="notifications" className="outline-none">
                    <AnimatePresence mode="wait">
                        <motion.div
                            key="notifications"
                            variants={itemVariants}
                            initial="hidden"
                            animate="visible"
                            exit="hidden"
                        >
                            <Card className="glass-card border-0 shadow-xl">
                                <CardHeader>
                                    <CardTitle className="flex items-center gap-2 font-bold font-display tracking-tight">
                                        <Bell className="h-5 w-5 text-primary" />
                                        Notification Preferences
                                    </CardTitle>
                                    <CardDescription className="font-medium">Choose how you want to receive notifications</CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-6">
                                    <div className="space-y-4">
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="email-notifications">Email Notifications</Label>
                                                <p className="text-sm text-muted-foreground">Receive notifications via email</p>
                                            </div>
                                            <Switch
                                                id="email-notifications"
                                                checked={notificationSettings.emailNotifications}
                                                onCheckedChange={(checked) => handleNotificationChange('emailNotifications', checked)}
                                            />
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="push-notifications">Push Notifications</Label>
                                                <p className="text-sm text-muted-foreground">Receive push notifications in browser</p>
                                            </div>
                                            <Switch
                                                id="push-notifications"
                                                checked={notificationSettings.pushNotifications}
                                                onCheckedChange={(checked) => handleNotificationChange('pushNotifications', checked)}
                                            />
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="marketing-emails">Marketing Emails</Label>
                                                <p className="text-sm text-muted-foreground">Receive promotional emails and updates</p>
                                            </div>
                                            <Switch
                                                id="marketing-emails"
                                                checked={notificationSettings.marketingEmails}
                                                onCheckedChange={(checked) => handleNotificationChange('marketingEmails', checked)}
                                            />
                                        </div>
                                        <Separator />
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="security-alerts">Security Alerts</Label>
                                                <p className="text-sm text-muted-foreground">Get notified about security events</p>
                                            </div>
                                            <Switch
                                                id="security-alerts"
                                                checked={notificationSettings.securityAlerts}
                                                onCheckedChange={(checked) => handleNotificationChange('securityAlerts', checked)}
                                            />
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="link-activity">Link Activity</Label>
                                                <p className="text-sm text-muted-foreground">Notifications about your links</p>
                                            </div>
                                            <Switch
                                                id="link-activity"
                                                checked={notificationSettings.linkActivity}
                                                onCheckedChange={(checked) => handleNotificationChange('linkActivity', checked)}
                                            />
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="weekly-digest">Weekly Digest</Label>
                                                <p className="text-sm text-muted-foreground">Weekly summary of your activity</p>
                                            </div>
                                            <Switch
                                                id="weekly-digest"
                                                checked={notificationSettings.weeklyDigest}
                                                onCheckedChange={(checked) => handleNotificationChange('weeklyDigest', checked)}
                                            />
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                    </AnimatePresence>
                </TabsContent>

                {/* Security Settings */}
                <TabsContent value="security" className="outline-none">
                    <AnimatePresence mode="wait">
                        <motion.div
                            key="security"
                            variants={itemVariants}
                            initial="hidden"
                            animate="visible"
                            exit="hidden"
                        >
                            <Card className="glass-card border-0 shadow-xl">
                                <CardHeader>
                                    <CardTitle className="flex items-center gap-2 font-bold font-display tracking-tight">
                                        <Shield className="h-5 w-5 text-primary" />
                                        Security Settings
                                    </CardTitle>
                                    <CardDescription className="font-medium">Manage your account security and privacy</CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-6">
                                    <div className="space-y-4">
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="two-factor">Two-Factor Authentication</Label>
                                                <p className="text-sm text-muted-foreground">Add an extra layer of security to your account</p>
                                            </div>
                                            <Switch
                                                id="two-factor"
                                                checked={securitySettings.twoFactorEnabled}
                                                onCheckedChange={(checked) => handleSecurityChange('twoFactorEnabled', checked)}
                                            />
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="session-timeout">Session Timeout</Label>
                                                <p className="text-sm text-muted-foreground">Automatically log out after inactivity</p>
                                            </div>
                                            <Switch
                                                id="session-timeout"
                                                checked={securitySettings.sessionTimeout}
                                                onCheckedChange={(checked) => handleSecurityChange('sessionTimeout', checked)}
                                            />
                                        </div>
                                        <div className="flex items-center justify-between">
                                            <div className="space-y-0.5">
                                                <Label htmlFor="login-alerts">Login Alerts</Label>
                                                <p className="text-sm text-muted-foreground">Get notified of new login attempts</p>
                                            </div>
                                            <Switch
                                                id="login-alerts"
                                                checked={securitySettings.loginAlerts}
                                                onCheckedChange={(checked) => handleSecurityChange('loginAlerts', checked)}
                                            />
                                        </div>
                                    </div>
                                    <Separator />
                                    <div className="space-y-4">
                                        <h4 className="text-sm font-medium">Password Management</h4>
                                        <div className="flex gap-2">
                                            <Button variant="outline" className="gap-2">
                                                <Lock className="h-4 w-4" />
                                                Change Password
                                            </Button>
                                            <Button variant="outline" className="gap-2">
                                                <RotateCcw className="h-4 w-4" />
                                                Reset Password
                                            </Button>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                    </AnimatePresence>
                </TabsContent>

                {/* Appearance Settings */}
                <TabsContent value="appearance" className="outline-none">
                    <AnimatePresence mode="wait">
                        <motion.div
                            key="appearance"
                            variants={itemVariants}
                            initial="hidden"
                            animate="visible"
                            exit="hidden"
                        >
                            <Card className="glass-card border-0 shadow-xl">
                                <CardHeader>
                                    <CardTitle className="flex items-center gap-2 font-bold font-display tracking-tight">
                                        <Palette className="h-5 w-5 text-primary" />
                                        Appearance Preferences
                                    </CardTitle>
                                    <CardDescription className="font-medium">Customize the look and feel of your interface</CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-6">
                                    <div className="space-y-4">
                                        <div>
                                            <Label htmlFor="theme">Theme</Label>
                                            <Select value={appearanceSettings.theme} onValueChange={(value) => handleAppearanceChange('theme', value)}>
                                                <SelectTrigger className="mt-1">
                                                    <SelectValue placeholder="Select theme" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem value="light" className="gap-2">
                                                        <div className="flex items-center gap-2">
                                                            <Sun className="h-4 w-4" />
                                                            Light
                                                        </div>
                                                    </SelectItem>
                                                    <SelectItem value="dark" className="gap-2">
                                                        <div className="flex items-center gap-2">
                                                            <Moon className="h-4 w-4" />
                                                            Dark
                                                        </div>
                                                    </SelectItem>
                                                    <SelectItem value="system" className="gap-2">
                                                        <div className="flex items-center gap-2">
                                                            <Monitor className="h-4 w-4" />
                                                            System
                                                        </div>
                                                    </SelectItem>
                                                </SelectContent>
                                            </Select>
                                        </div>
                                        <div>
                                            <Label htmlFor="language">Language</Label>
                                            <Select value={appearanceSettings.language} onValueChange={(value) => handleAppearanceChange('language', value)}>
                                                <SelectTrigger className="mt-1">
                                                    <SelectValue placeholder="Select language" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem value="en">English</SelectItem>
                                                    <SelectItem value="es">Español</SelectItem>
                                                    <SelectItem value="fr">Français</SelectItem>
                                                    <SelectItem value="de">Deutsch</SelectItem>
                                                    <SelectItem value="zh">中文</SelectItem>
                                                </SelectContent>
                                            </Select>
                                        </div>
                                        <div>
                                            <Label htmlFor="date-format">Date Format</Label>
                                            <Select value={appearanceSettings.dateFormat} onValueChange={(value) => handleAppearanceChange('dateFormat', value)}>
                                                <SelectTrigger className="mt-1">
                                                    <SelectValue placeholder="Select date format" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem value="MM/DD/YYYY">MM/DD/YYYY</SelectItem>
                                                    <SelectItem value="DD/MM/YYYY">DD/MM/YYYY</SelectItem>
                                                    <SelectItem value="YYYY-MM-DD">YYYY-MM-DD</SelectItem>
                                                </SelectContent>
                                            </Select>
                                        </div>
                                        <div>
                                            <Label htmlFor="timezone">Timezone</Label>
                                            <Select value={appearanceSettings.timezone} onValueChange={(value) => handleAppearanceChange('timezone', value)}>
                                                <SelectTrigger className="mt-1">
                                                    <SelectValue placeholder="Select timezone" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem value="UTC">UTC</SelectItem>
                                                    <SelectItem value="America/New_York">Eastern Time</SelectItem>
                                                    <SelectItem value="America/Chicago">Central Time</SelectItem>
                                                    <SelectItem value="America/Denver">Mountain Time</SelectItem>
                                                    <SelectItem value="America/Los_Angeles">Pacific Time</SelectItem>
                                                    <SelectItem value="Europe/London">London</SelectItem>
                                                    <SelectItem value="Europe/Paris">Paris</SelectItem>
                                                    <SelectItem value="Asia/Tokyo">Tokyo</SelectItem>
                                                </SelectContent>
                                            </Select>
                                        </div>
                                    </div>
                                </CardContent>
                            </Card>
                        </motion.div>
                    </AnimatePresence>
                </TabsContent>
            </Tabs>

            {/* Save Button */}
            <motion.div variants={itemVariants} className="mt-8 flex justify-end">
                <Button onClick={handleSaveSettings} size="lg" className="gap-2 font-bold font-display px-8 shadow-lg shadow-primary/20">
                    <Save className="h-4 w-4" />
                    Save All Settings
                </Button>
            </motion.div>
        </motion.div>
    );
}
