'use client';

import { useEffect, useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { checkAuth } from '@/store/slices/authSlice';
import { AppDispatch, RootState } from '@/store';

export function AuthInitializer() {
    const dispatch = useDispatch<AppDispatch>();
    const { isAuthenticated, isInitialized } = useSelector((state: RootState) => state.auth);
    const hasChecked = useRef(false);

    useEffect(() => {
        // Skip if already authenticated or already checked
        // This prevents double API calls after login
        if (hasChecked.current || isAuthenticated) {
            return;
        }
        hasChecked.current = true;
        dispatch(checkAuth());
    }, [dispatch, isAuthenticated]);

    return null;
}
