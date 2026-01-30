import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */

  async rewrites() {
    return [
      {
        source: "/v1/api/:path*",
        destination: "http://localhost:8090/v1/api/:path*",
      },
    ];
  },
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'images.unsplash.com',
      },
      {
        protocol: 'https',
        hostname: 'plus.unsplash.com',
      },
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '',
      },
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '8092',
      },
      {
        protocol: 'http',
        hostname: '127.0.0.1',
        port: '',
      },
      {
        protocol: 'http',
        hostname: '127.0.0.1',
        port: '8092',
      },
      {
        protocol: 'http',
        hostname: 'localhost',
        port: '9000',
      },
      {
        protocol: 'http',
        hostname: '127.0.0.1',
        port: '9000',
      },
    ],
  },
};

export default nextConfig;
