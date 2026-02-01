import { ScheduledPost } from '@/services/scheduler';
import { useDraggable } from '@dnd-kit/core';
import { cn } from '@/lib/utils';
import Image from 'next/image';
import { FaInstagram, FaLinkedin, FaTwitter, FaFacebook, FaTiktok } from 'react-icons/fa';

interface ScheduledPostCardProps {
    post: ScheduledPost;
}

export const ScheduledPostCard = ({ post }: ScheduledPostCardProps) => {
    const { attributes, listeners, setNodeRef, transform, isDragging } = useDraggable({
        id: post.id,
        data: {
            type: 'post',
            post,
        },
    });

    const style = transform ? {
        transform: `translate3d(${transform.x}px, ${transform.y}px, 0)`,
    } : undefined;

    const getIcon = (platform: string) => {
        switch (platform) {
            case 'instagram': return <FaInstagram className="text-pink-600" />;
            case 'linkedin': return <FaLinkedin className="text-blue-700" />;
            case 'twitter': return <FaTwitter className="text-sky-500" />;
            case 'facebook': return <FaFacebook className="text-blue-600" />;
            case 'tiktok': return <FaTiktok className="text-black dark:text-white" />;
            default: return null;
        }
    };

    return (
        <div
            ref={setNodeRef}
            style={style}
            {...listeners}
            {...attributes}
            className={cn(
                "group relative mt-1 flex cursor-grab select-none items-center gap-2 overflow-hidden rounded-md border bg-card p-1.5 shadow-sm transition-all hover:bg-accent hover:shadow-md",
                isDragging && "opacity-50 ring-2 ring-primary"
            )}
        >
            {/* Thumbnail */}
            <div className="relative h-8 w-8 shrink-0 overflow-hidden rounded-sm bg-muted">
                <Image
                    src={post.mediaType === 'video' ? post.mediaUrl : post.mediaUrl}
                    alt="Post preview"
                    fill
                    className="object-cover"
                    sizes="32px"
                />
            </div>

            {/* Content */}
            <div className="flex min-w-0 flex-1 flex-col justify-center">
                <div className="flex items-center gap-1.5 ">
                    {post.platforms.map((p) => (
                        <span key={p} className="text-[10px]">{getIcon(p)}</span>
                    ))}
                </div>
                <p className="truncate text-[10px] text-muted-foreground font-medium">
                    {new Date(post.scheduledAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </p>
            </div>
        </div>
    );
};
