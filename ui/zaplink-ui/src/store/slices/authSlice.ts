import { AuthApi } from "@/services/auth";
import {
	AuthState,
	LoginRequest,
	RegisterRequest,
	ForgotPasswordRequest,
	ResetPasswordRequest,
	UserInfo,
	VerifyEmailResponse
} from "@/lib/types/apiRequestType";
import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import Cookies from "js-cookie";

const initialState: AuthState = {
	user: null,
	token: null,
	refreshToken: null,
	isAuthenticated: false,
	isLoading: false,
	isInitialized: false,
	error: null,
};

// Async Thunks
export const loginUser = createAsyncThunk(
	"auth/login",
	async (data: LoginRequest, { rejectWithValue }) => {
		try {
			const response = await AuthApi.login(data);
			localStorage.setItem("token", response.accessToken);
			localStorage.setItem("refreshToken", response.refreshToken);
			return response;
		} catch (error: any) {
			return rejectWithValue(
				error.response?.data?.message || "Login failed"
			);
		}
	}
);

export const registerUser = createAsyncThunk(
	"auth/register",
	async (data: RegisterRequest, { rejectWithValue }) => {
		try {
			const response = await AuthApi.register(data);
			return response;
		} catch (error: any) {
			return rejectWithValue(
				error.response?.data?.message || "Registration failed"
			);
		}
	}
);

export const forgotPassword = createAsyncThunk(
	"auth/forgotPassword",
	async (data: ForgotPasswordRequest, { rejectWithValue }) => {
		try {
			const response = await AuthApi.forgotPassword(data);
			return response;
		} catch (error: any) {
			return rejectWithValue(
				error.response?.data?.message || "Forgot password request failed"
			);
		}
	}
);

export const resetPassword = createAsyncThunk(
	"auth/resetPassword",
	async (data: ResetPasswordRequest, { rejectWithValue }) => {
		try {
			const response = await AuthApi.resetPassword(data);
			return response;
		} catch (error: any) {
			return rejectWithValue(
				error.response?.data?.message || "Password reset failed"
			);
		}
	}
);

export const verifyEmail = createAsyncThunk(
	"auth/verifyEmail",
	async (token: string, { rejectWithValue }) => {
		try {
			const response = await AuthApi.verifyEmail(token);
			return response;
		} catch (error: any) {
			return rejectWithValue(
				error.response?.data?.message || "Email verification failed"
			);
		}
	}
);

export const resendVerification = createAsyncThunk(
	"auth/resendVerification",
	async (email: string, { rejectWithValue }) => {
		try {
			const response = await AuthApi.resendVerification(email);
			return response;
		} catch (error: any) {
			return rejectWithValue(
				error.response?.data?.message || "Resend verification failed"
			);
		}
	}
);

export const checkAuth = createAsyncThunk(
	"auth/checkAuth",
	async (_, { rejectWithValue }) => {
		// Prevent calling API if no token exists locally
		if (typeof window !== 'undefined' && !localStorage.getItem("token")) {
			return rejectWithValue("No token found");
		}

		try {
			const user = await AuthApi.getCurrentUser();
			return user;
		} catch (error: any) {
			localStorage.removeItem("token");
			localStorage.removeItem("refreshToken");
			Cookies.remove("token", { path: '/' });
			Cookies.remove("refreshToken", { path: '/' });
			Cookies.remove("token");
			Cookies.remove("refreshToken");
			return rejectWithValue(
				error.response?.data?.message || "Session expired"
			);
		}
	}
);

const authSlice = createSlice({
	name: "auth",
	initialState,
	reducers: {
		logout: (state) => {
			state.user = null;
			state.token = null;
			state.refreshToken = null;
			state.isAuthenticated = false;
			state.isInitialized = true; // Ensure app knows we are done checking
			state.error = null;
			localStorage.removeItem("token");
			localStorage.removeItem("refreshToken");

			// Clear cookies robustly
			Cookies.remove("token");
			Cookies.remove("refreshToken");
			// Also try removing with path and domain options just in case
			Cookies.remove("token", { path: '/' });
			Cookies.remove("refreshToken", { path: '/' });
		},
		clearError: (state) => {
			state.error = null;
		},
		setInitialized: (state, action: PayloadAction<boolean>) => {
			state.isInitialized = action.payload;
		},
	},
	extraReducers: (builder) => {
		// Login
		builder.addCase(loginUser.pending, (state) => {
			state.isLoading = true;
			state.error = null;
		});
		builder.addCase(loginUser.fulfilled, (state, action) => {
			state.isLoading = false;
			state.isAuthenticated = true;
			state.user = action.payload.userInfo;
			state.token = action.payload.accessToken;
			state.refreshToken = action.payload.refreshToken;

			// Set cookies for middleware
			// Set cookies for middleware
			Cookies.set("token", action.payload.accessToken, { expires: 7, path: '/', sameSite: 'Strict' });
			Cookies.set("refreshToken", action.payload.refreshToken, { expires: 30, path: '/', sameSite: 'Strict' });
		});
		builder.addCase(loginUser.rejected, (state, action) => {
			state.isLoading = false;
			state.error = action.payload as string;
		});

		// Register
		builder.addCase(registerUser.pending, (state) => {
			state.isLoading = true;
			state.error = null;
		});
		builder.addCase(registerUser.fulfilled, (state) => {
			state.isLoading = false;
			// Registration doesn't automatically login, user needs to verify email
		});
		builder.addCase(registerUser.rejected, (state, action) => {
			state.isLoading = false;
			state.error = action.payload as string;
		});

		// Verify Email
		builder.addCase(verifyEmail.pending, (state) => {
			state.isLoading = true;
			state.error = null;
		});
		builder.addCase(verifyEmail.fulfilled, (state, action: PayloadAction<VerifyEmailResponse>) => {
			state.isLoading = false;
			if (action.payload.accessToken && action.payload.refreshToken && action.payload.userInfo) {
				state.isAuthenticated = true;
				state.user = action.payload.userInfo;
				state.token = action.payload.accessToken;
				state.refreshToken = action.payload.refreshToken;

				// Set cookies for middleware
				Cookies.set("token", action.payload.accessToken, { expires: 7, path: '/', sameSite: 'Strict' });
				Cookies.set("refreshToken", action.payload.refreshToken, { expires: 30, path: '/', sameSite: 'Strict' });
			}
		});
		builder.addCase(verifyEmail.rejected, (state, action) => {
			state.isLoading = false;
			state.error = action.payload as string;
		});

		// Check Auth
		builder.addCase(checkAuth.pending, (state) => {
			state.isLoading = true;
		});
		builder.addCase(checkAuth.fulfilled, (state, action) => {
			state.isLoading = false;
			state.isAuthenticated = true;
			state.user = action.payload;
			state.isInitialized = true;
		});
		builder.addCase(checkAuth.rejected, (state) => {
			state.isLoading = false;
			state.isAuthenticated = false;
			state.user = null;
			state.token = null;
			state.refreshToken = null;
			state.isInitialized = true;
		});

		// Other generic pending/rejected handlers can be added if needed, 
		// but for now individual handling is fine or we can group them.
		builder.addMatcher(
			(action) => action.type.endsWith('/pending') && !action.type.includes('checkAuth'),
			(state) => {
				state.isLoading = true;
				state.error = null;
			}
		);
		builder.addMatcher(
			(action) => action.type.endsWith('/rejected') && !action.type.includes('checkAuth'),
			(state, action: any) => {
				state.isLoading = false;
				state.error = action.payload as string;
			}
		);
		builder.addMatcher(
			(action) => action.type.endsWith('/fulfilled'),
			(state) => {
				state.isLoading = false;
			}
		);
	},
});

export const { logout, clearError, setInitialized } = authSlice.actions;
export default authSlice.reducer;
