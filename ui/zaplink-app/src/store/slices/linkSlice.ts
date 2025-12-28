import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface ShortLink {
  id: string;
  originalUrl: string;
  shortUrl: string;
  clickCount: number;
  createdAt: string;
}

interface LinkState {
  links: ShortLink[];
  recentLinks: ShortLink[];
  selectedLink: ShortLink | null;
  isLoading: boolean;
  error: string | null;
}

const initialState: LinkState = {
  links: [],
  recentLinks: [],
  selectedLink: null,
  isLoading: false,
  error: null,
};

const linkSlice = createSlice({
  name: 'links',
  initialState,
  reducers: {
    setLinks: (state, action: PayloadAction<ShortLink[]>) => {
      state.links = action.payload;
    },
    addLink: (state, action: PayloadAction<ShortLink>) => {
      state.links.unshift(action.payload);
      state.recentLinks.unshift(action.payload);
      if (state.recentLinks.length > 5) {
        state.recentLinks.pop();
      }
    },
    removeLink: (state, action: PayloadAction<string>) => {
      state.links = state.links.filter(link => link.id !== action.payload);
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
    setSelectedLink: (state, action: PayloadAction<ShortLink | null>) => {
      state.selectedLink = action.payload;
    }
  },
});

export const { setLinks, addLink, removeLink, setLoading, setError, setSelectedLink } = linkSlice.actions;
export default linkSlice.reducer;
