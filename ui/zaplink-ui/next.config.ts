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
};

export default nextConfig;
