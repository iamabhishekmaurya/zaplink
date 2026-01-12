// Authentication middleware - handles route protection based on user authentication status
// This is not a proxy middleware, so the deprecation warning does not apply
import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export async function middleware(request: NextRequest) {
    let token = request.cookies.get('token')?.value;

    // Check for "bad" token strings that might be stuck in cookies
    if (token === 'undefined' || token === 'null' || token === '') {
        token = undefined;
    }

    const isAuthPage = request.nextUrl.pathname.startsWith('/login') ||
        request.nextUrl.pathname.startsWith('/signup') ||
        request.nextUrl.pathname.startsWith('/forgot-password') ||
        request.nextUrl.pathname.startsWith('/reset-password') ||
        request.nextUrl.pathname.startsWith('/verify-email') ||
        request.nextUrl.pathname.startsWith('/verify-account');

    const isDashboardPage = request.nextUrl.pathname.startsWith('/dashboard');

    if (isDashboardPage && !token) {
        return NextResponse.redirect(new URL('/login', request.url));
    }

    if (isDashboardPage && token) {
        // Skip backend validation for now - just check if token exists
        return NextResponse.next();
    }

    if (isAuthPage && token) {
        return NextResponse.redirect(new URL('/dashboard', request.url));
    }

    return NextResponse.next();
}

export const config = {
    matcher: ['/dashboard/:path*', '/login', '/signup', '/forgot-password', '/reset-password', '/verify-email', '/verify-account'],
};
