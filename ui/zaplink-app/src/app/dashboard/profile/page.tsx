'use client';

import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { RootState } from '@/store';
import { setUser } from '@/store/slices/authSlice';
import api from '@/utils/api';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';
import { User, Mail, Calendar, Shield, Edit2, Save, X } from 'lucide-react';
import { motion } from 'framer-motion';
import { toast } from 'sonner'; // Assuming sonner is used, if not remove or check dependencies, but user mentioned profile sync so I'll try to use toast if available or just console. Actually I'll check imports. No imports for toast found in original file. I'll stick to console or alert for now, or check generic ui components. I will stick to console for simplicity unless I see toast hook usage elsewhere.

export default function ProfilePage() {
    const dispatch = useDispatch();
    const { user } = useSelector((state: RootState) => state.auth);
    const [isEditing, setIsEditing] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [formData, setFormData] = useState({
        username: user?.username || '',
        email: user?.email || '',
        firstName: user?.firstName || '',
        lastName: user?.lastName || '',
        phoneNumber: user?.phoneNumber || '',
    });

    // Sync form data when user changes (e.g. after fetch)
    useEffect(() => {
        if (user) {
            setFormData({
                username: user.username || '',
                email: user.email || '',
                firstName: user.firstName || '',
                lastName: user.lastName || '',
                phoneNumber: user.phoneNumber || '',
            });
        }
    }, [user]);

    // Fetch latest user data on mount
    useEffect(() => {
        const fetchProfile = async () => {
            if (!user?.id) return;
            try {
                const response = await api.get(`/v1/api/users/${user.id}`);
                if (response.data) {
                    dispatch(setUser(response.data));
                }
            } catch (error) {
                console.error("Failed to fetch profile:", error);
            }
        };
        fetchProfile();
    }, [dispatch, user?.id]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSave = async () => {
        if (!user?.id) return;
        setIsLoading(true);
        try {
            // Only send allowed fields for update
            const updatePayload = {
                firstName: formData.firstName,
                lastName: formData.lastName,
                phoneNumber: formData.phoneNumber
            };

            await api.put(`/v1/api/users/${user.id}`, updatePayload);

            // Refresh profile data
            const response = await api.get(`/v1/api/users/${user.id}`);
            dispatch(setUser(response.data));

            setIsEditing(false);
            // toast.success("Profile updated successfully");
        } catch (error) {
            console.error("Failed to update profile:", error);
            // toast.error("Failed to update profile");
        } finally {
            setIsLoading(false);
        }
    };

    const handleCancel = () => {
        setFormData({
            username: user?.username || '',
            email: user?.email || '',
            firstName: user?.firstName || '',
            lastName: user?.lastName || '',
            phoneNumber: user?.phoneNumber || '',
        });
        setIsEditing(false);
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
                <h1 className="text-3xl font-bold font-display tracking-tight text-foreground mb-2">Profile</h1>
                <p className="text-muted-foreground font-medium">Manage your personal information and account details</p>
            </div>

            <div className="grid gap-6">
                {/* Profile Overview Card */}
                <motion.div variants={itemVariants}>
                    <Card className="glass-card border-0 shadow-xl">
                        <CardHeader className="pb-6">
                            <div className="flex items-center justify-between">
                                <div className="flex items-center gap-4">
                                    <Avatar className="h-20 w-20">
                                        <AvatarImage src="" alt={user?.username} />
                                        <AvatarFallback className="bg-gradient-to-r from-primary to-accent text-white text-xl font-semibold">
                                            {user?.username?.charAt(0).toUpperCase()}
                                        </AvatarFallback>
                                    </Avatar>
                                    <div>
                                        <CardTitle className="text-2xl font-bold font-display tracking-tight">{user?.username}</CardTitle>
                                        <CardDescription className="flex items-center gap-2 mt-1 font-medium">
                                            <Mail className="h-4 w-4" />
                                            {user?.email}
                                        </CardDescription>
                                        <div className="flex items-center gap-2 mt-2">
                                            <Badge variant={user?.verified ? 'default' : 'secondary'}>
                                                {user?.verified ? 'Verified' : 'Unverified'}
                                            </Badge>
                                            <Badge variant={user?.active ? 'default' : 'destructive'}>
                                                {user?.active ? 'Active' : 'Inactive'}
                                            </Badge>
                                        </div>
                                    </div>
                                </div>
                                <Button
                                    onClick={() => setIsEditing(!isEditing)}
                                    variant="outline"
                                    size="sm"
                                    className="gap-2 font-bold font-display"
                                    disabled={isLoading}
                                >
                                    {isEditing ? <X className="h-4 w-4" /> : <Edit2 className="h-4 w-4" />}
                                    {isEditing ? 'Cancel' : 'Edit Profile'}
                                </Button>
                            </div>
                        </CardHeader>
                        <CardContent>
                            <div className="grid md:grid-cols-2 gap-6">
                                <div className="space-y-4">
                                    <div>
                                        <Label htmlFor="username">Username</Label>
                                        <Input
                                            id="username"
                                            name="username"
                                            value={formData.username}
                                            onChange={handleInputChange}
                                            disabled={true} // Username cannot be changed
                                            className="mt-1"
                                        />
                                    </div>
                                    <div>
                                        <Label htmlFor="email">Email</Label>
                                        <Input
                                            id="email"
                                            name="email"
                                            type="email"
                                            value={formData.email}
                                            onChange={handleInputChange}
                                            disabled={true} // Email cannot be changed here
                                            className="mt-1"
                                        />
                                    </div>
                                    <div>
                                        <Label htmlFor="phoneNumber">Phone Number</Label>
                                        <Input
                                            id="phoneNumber"
                                            name="phoneNumber"
                                            value={formData.phoneNumber}
                                            onChange={handleInputChange}
                                            disabled={!isEditing}
                                            className="mt-1"
                                        />
                                    </div>
                                </div>
                                <div className="space-y-4">
                                    <div>
                                        <Label htmlFor="firstName">First Name</Label>
                                        <Input
                                            id="firstName"
                                            name="firstName"
                                            value={formData.firstName}
                                            onChange={handleInputChange}
                                            disabled={!isEditing}
                                            className="mt-1"
                                        />
                                    </div>
                                    <div>
                                        <Label htmlFor="lastName">Last Name</Label>
                                        <Input
                                            id="lastName"
                                            name="lastName"
                                            value={formData.lastName}
                                            onChange={handleInputChange}
                                            disabled={!isEditing}
                                            className="mt-1"
                                        />
                                    </div>
                                </div>
                            </div>
                            {isEditing && (
                                <div className="flex justify-end gap-2 mt-6">
                                    <Button onClick={handleCancel} variant="outline" className="font-bold font-display" disabled={isLoading}>
                                        Cancel
                                    </Button>
                                    <Button onClick={handleSave} className="gap-2 font-bold font-display shadow-lg shadow-primary/20" disabled={isLoading}>
                                        <Save className="h-4 w-4" />
                                        {isLoading ? 'Saving...' : 'Save Changes'}
                                    </Button>
                                </div>
                            )}
                        </CardContent>
                    </Card>
                </motion.div>

                {/* Account Information */}
                <motion.div variants={itemVariants}>
                    <Card className="glass-card border-0 shadow-xl">
                        <CardHeader>
                            <CardTitle className="flex items-center gap-2 font-bold font-display tracking-tight">
                                <Shield className="h-5 w-5 text-primary" />
                                Account Information
                            </CardTitle>
                            <CardDescription className="font-medium">View your account status and security details</CardDescription>
                        </CardHeader>
                        <CardContent className="space-y-4">
                            <div className="grid md:grid-cols-2 gap-4">
                                <div className="flex items-center justify-between p-4 rounded-lg bg-muted/50">
                                    <div className="flex items-center gap-3">
                                        <User className="h-5 w-5 text-muted-foreground" />
                                        <div>
                                            <p className="font-medium">Account ID</p>
                                            <p className="text-sm text-muted-foreground">#{user?.id}</p>
                                        </div>
                                    </div>
                                </div>
                                <div className="flex items-center justify-between p-4 rounded-lg bg-muted/50">
                                    <div className="flex items-center gap-3">
                                        <Calendar className="h-5 w-5 text-muted-foreground" />
                                        <div>
                                            <p className="font-medium">Member Since</p>
                                            <p className="text-sm text-muted-foreground">
                                                {user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <Separator />
                            <div className="grid md:grid-cols-2 gap-4">
                                <div className="p-4 rounded-lg bg-muted/50">
                                    <p className="font-medium mb-2">Email Verification</p>
                                    <p className="text-sm text-muted-foreground mb-2">
                                        Status: {user?.verified ? 'Verified' : 'Not Verified'}
                                    </p>
                                    {!user?.verified && (
                                        <Button size="sm" variant="outline">
                                            Send Verification Email
                                        </Button>
                                    )}
                                </div>
                                <div className="p-4 rounded-lg bg-muted/50">
                                    <p className="font-medium mb-2">Account Status</p>
                                    <p className="text-sm text-muted-foreground mb-2">
                                        Status: {user?.active ? 'Active' : 'Inactive'}
                                    </p>
                                    <Badge variant={user?.active ? 'default' : 'destructive'}>
                                        {user?.active ? 'Active' : 'Inactive'}
                                    </Badge>
                                </div>
                            </div>
                        </CardContent>
                    </Card>
                </motion.div>
            </div>
        </motion.div>
    );
}
