import api from "@/services/client";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { API_BASE_URLS } from "@/lib/constants/apiConstant";

interface Folder {
    id: string;
    name: string;
    parentId?: string;
    ownerId: string;
}

interface Asset {
    id: string;
    filename: string;
    url: string;
    mimeType: string;
    sizeBytes: number;
    thumbnailPath?: string;
    folder?: {
        id: string;
    };
    createdAt: string;
}

interface CreateFolderPayload {
    name: string;
    ownerId: string;
    parentId?: string;
}

export const useFolders = (ownerId: string, parentId?: string | null) => {
    return useQuery({
        queryKey: ["folders", ownerId, parentId],
        queryFn: async () => {
            const params: any = { ownerId };
            if (parentId) params.parentId = parentId;
            const res = await api.get<Folder[]>(`${API_BASE_URLS.MEDIA}/folders`, { params });
            return res.data;
        },
        enabled: !!ownerId,
    });
};

export const useAssets = (ownerId: string, folderId?: string | null) => {
    return useQuery({
        queryKey: ["assets", ownerId, folderId],
        queryFn: async () => {
            const params: any = { ownerId };
            if (folderId) params.folderId = folderId;
            const res = await api.get<{ content: Asset[] }>(`${API_BASE_URLS.MEDIA}`, { params });
            return res.data.content;
        },
        enabled: !!ownerId,
    });
};

export const useCreateFolder = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async (payload: CreateFolderPayload) => {
            const res = await api.post<Folder>(`${API_BASE_URLS.MEDIA}/folders`, payload);
            return res.data;
        },
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ["folders", variables.ownerId, variables.parentId] });
        },
    });
};

export const useUploadAsset = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async ({ file, ownerId, folderId }: { file: File; ownerId: string; folderId?: string }) => {
            const formData = new FormData();
            formData.append("file", file);
            formData.append("ownerId", ownerId);
            if (folderId) formData.append("folderId", folderId);
            const res = await api.post<Asset>(`${API_BASE_URLS.MEDIA}/upload`, formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });
            return res.data;
        },
        onSuccess: (data, variables) => {
            queryClient.invalidateQueries({ queryKey: ["assets", variables.ownerId, variables.folderId] });
        },
    });
};

export const useDeleteAsset = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async (id: string) => {
            await api.delete(`${API_BASE_URLS.MEDIA}/${id}`);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["assets"] });
        },
    });
};

export const useDeleteFolder = () => {
    const queryClient = useQueryClient();
    return useMutation({
        mutationFn: async (id: string) => {
            await api.delete(`${API_BASE_URLS.MEDIA}/folders/${id}`);
        },
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ["folders"] });
        },
    });
};
