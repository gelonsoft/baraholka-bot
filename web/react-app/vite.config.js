import {defineConfig} from 'vite';
import react from '@vitejs/plugin-react';
import tsconfigPaths from 'vite-tsconfig-paths'
import svgrPlugin from 'vite-plugin-svgr';
import postcssNested from 'postcss-nested';

export default defineConfig(() => {
    return {
        server: {
            port: 4000,
            open: true,
        },
        build: {
            outDir: 'build',
            minify: process.env.mode !== "development",
        },
        plugins: [react(
        ), tsconfigPaths(), svgrPlugin()],
        css: {
            postcss: {
                plugins: [postcssNested]
            }
        },
    };
});