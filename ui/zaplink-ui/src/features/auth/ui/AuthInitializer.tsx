'use client';

import { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { checkAuth } from '@/store/slices/authSlice';
import { AppDispatch } from '@/store';

export function AuthInitializer() {
    const dispatch = useDispatch<AppDispatch>();

    useEffect(() => {
        dispatch(checkAuth());
    }, [dispatch]);

    return null;
}
