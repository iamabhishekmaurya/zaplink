'use client';

import { Provider } from 'react-redux';
import { store } from '@/store';
import React, { useEffect } from 'react';
import { useAuth } from '@/hooks/useAuth';

function AuthInitializer({ children }: { children: React.ReactNode }) {
    const { checkAuth } = useAuth();

    useEffect(() => {
        checkAuth();
    }, []); // Only run once on mount

    return <>{children}</>;
}

export default function StoreProvider({ children }: { children: React.ReactNode }) {
    return (
        <Provider store={store}>
            <AuthInitializer>
                {children}
            </AuthInitializer>
        </Provider>
    );
}
