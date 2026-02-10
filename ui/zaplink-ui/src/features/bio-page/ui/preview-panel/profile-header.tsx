"use client";

import { motion } from "framer-motion";
import { ThemeConfig } from "@/ui/design-system/theme-utils";
import { cn } from "@/lib/utils";
import { CheckCircle2, Sparkles } from "lucide-react";

interface ProfileHeaderProps {
  avatarUrl?: string;
  title?: string;
  username: string;
  bioText?: string;
  verified?: boolean;
  theme: ThemeConfig;
}

export function ProfileHeader({
  avatarUrl,
  title,
  username,
  bioText,
  verified = false,
  theme,
}: ProfileHeaderProps) {
  const avatarShape = theme.layout?.buttonShape === 'square' ? 'rounded-lg' : 'rounded-full';

  return (
    <div className="flex flex-col items-center text-center">
      {/* Avatar Container */}
      <motion.div
        initial={{ scale: 0, rotate: -180 }}
        animate={{ scale: 1, rotate: 0 }}
        transition={{ duration: 0.5, type: "spring", stiffness: 200 }}
        className="relative mb-6"
      >
        {/* Glow Effect */}
        <div className="absolute inset-0 bg-gradient-to-r from-pink-500 via-purple-500 to-cyan-500 rounded-full blur-xl opacity-50 animate-pulse" />

        {/* Avatar */}
        <div
          className={cn(
            "relative w-28 h-28 overflow-hidden border-4 border-white/20 shadow-2xl",
            avatarShape
          )}
        >
          {avatarUrl ? (
            <img
              src={avatarUrl}
              alt={title || username}
              className="w-full h-full object-cover"
            />
          ) : (
            <div className="w-full h-full bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center">
              <span className="text-3xl font-bold text-white">
                {username.charAt(0).toUpperCase()}
              </span>
            </div>
          )}
        </div>

        {/* Verified Badge */}
        {verified && (
          <motion.div
            initial={{ scale: 0 }}
            animate={{ scale: 1 }}
            transition={{ delay: 0.3, type: "spring" }}
            className="absolute -bottom-1 -right-1 w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center border-4 border-white/20 shadow-lg"
          >
            <CheckCircle2 className="w-5 h-5 text-white" />
          </motion.div>
        )}
      </motion.div>

      {/* Title */}
      <motion.h1
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.2 }}
        className="text-2xl sm:text-3xl font-bold text-white mb-2 flex items-center gap-2"
      >
        {title || `@${username}`}
        {verified && (
          <Sparkles className="w-5 h-5 text-yellow-400 animate-pulse" />
        )}
      </motion.h1>

      {/* Username */}
      <motion.p
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.3 }}
        className="text-white/60 text-sm font-medium mb-4"
      >
        @{username}
      </motion.p>

      {/* Bio */}
      {bioText && (
        <motion.p
          initial={{ opacity: 0, y: 10 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.4 }}
          className="text-white/80 text-base max-w-md leading-relaxed whitespace-pre-wrap"
        >
          {bioText}
        </motion.p>
      )}
    </div>
  );
}
