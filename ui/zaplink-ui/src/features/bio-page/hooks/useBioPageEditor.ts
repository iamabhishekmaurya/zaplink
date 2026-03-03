import { useTheme } from '@/features/bio-page/hooks/useTheme';
import { BioPageWithTheme } from '@/features/bio-page/types/index';
import { BioPage, bioPageService } from '@/services/bioPageService';
import { useCallback, useEffect, useState, useRef } from 'react';
import { toast } from 'sonner';
import { useDebouncedCallback } from '@/hooks/use-debounce';

export function useBioPageEditor(initialData?: BioPage) {
    // Safety check for initialData with fallback
    const safeInitialData: BioPage = initialData || {
        id: '0',
        username: '',
        ownerId: '',
        bioText: '',
        avatarUrl: '',
        themeConfig: { palette: 'minimal' },
        title: '',
        coverUrl: '',
        seoMeta: undefined,
        isPublic: true,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        bioLinks: [],
        effects: {
            backgroundType: 'solid'
        }
    };

    const [data, setData] = useState<BioPage>(safeInitialData);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const [hasUnsavedChanges, setHasUnsavedChanges] = useState<boolean>(false);
    const prevSerializedTheme = useRef<string>('');
    const pageIdRef = useRef<string>(safeInitialData.id);

    // Keep ref in sync with current ID
    useEffect(() => {
        pageIdRef.current = data.id;
    }, [data.id]);

    // Initialize theme hook with the initial JSON from data
    const {
        theme,
        updateTheme,
        serializedTheme,
        resetTheme,
        cssVariables
    } = useTheme(JSON.stringify(safeInitialData.themeConfig || {}));

    // Debounced theme update to prevent excessive re-renders
    const debouncedThemeUpdate = useDebouncedCallback((newSerializedTheme: string) => {
        try {
            const parsed = JSON.parse(newSerializedTheme);
            setData(prev => ({ ...prev, themeConfig: parsed }));
            setHasUnsavedChanges(true);
        } catch {
            console.error('Failed to parse theme update');
        }
    }, 300, []);

    // Update local data when theme changes (debounced)
    useEffect(() => {
        const currentThemeString = JSON.stringify(data.themeConfig || {});
        if (serializedTheme !== prevSerializedTheme.current && serializedTheme !== currentThemeString) {
            debouncedThemeUpdate(serializedTheme);
            prevSerializedTheme.current = serializedTheme;
        }
    }, [serializedTheme, data.themeConfig, debouncedThemeUpdate]);

    const updateField = useCallback((field: keyof BioPage, value: any) => {
        setData(prev => {
            if (prev[field] === value) return prev;
            return { ...prev, [field]: value };
        });
        setHasUnsavedChanges(true);
    }, []);

    const refresh = useCallback(async () => {
        const currentId = pageIdRef.current;
        if (!currentId || currentId === '0') {
            return;
        }

        try {
            const freshData = await bioPageService.getBioPageById(String(currentId));
            setData(prev => ({ ...prev, ...freshData }));
        } catch {
            // Silent fail - user will see error toast if needed
        }
    }, []);

    const save = useCallback(async () => {
        if (!data.id || data.id === '0') {
            toast.error("Cannot save: Invalid page ID");
            return;
        }

        setIsSaving(true);
        try {
            await bioPageService.updateBioPage(data.id, {
                title: data.title,
                bio_text: data.bioText,
                theme_config: serializedTheme,
                avatar_url: data.avatarUrl,
                is_public: data.isPublic ?? true,
                seo_meta: data.seoMeta ? JSON.stringify(data.seoMeta) : undefined
            });

            // Also save the latest link order context
            // If the user reordered links locally, this call persists that new order to the backend
            if (data.bioLinks && data.bioLinks.length > 0) {
                await bioPageService.reorderLinks(data.id, data.bioLinks.map(l => l.id));
            }

            setHasUnsavedChanges(false);
            toast.success("Changes saved successfully");
        } catch {
            toast.error("Failed to save changes");
        } finally {
            setIsSaving(false);
        }
    }, [data, serializedTheme]);

    // Combine data and parsed theme for the renderer
    const bioPageWithTheme: BioPageWithTheme = {
        ...data,
        parsedTheme: theme
    };

    return {
        data,
        bioPageWithTheme,
        isLoading,
        isSaving,
        hasUnsavedChanges,
        updateField,
        updateTheme,
        resetTheme,
        save,
        refresh,
        setData // Expose raw setter if needed
    };
}
