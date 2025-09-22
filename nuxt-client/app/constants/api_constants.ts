const runtimeConfig = useRuntimeConfig();

/**
 * Base URL for the API.
 * Only work in server side.
 */
export const API_BASE_URL = runtimeConfig.URL;
