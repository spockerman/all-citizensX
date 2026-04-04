import type { Metadata } from "next";
import { Inter } from "next/font/google";
import { Providers } from "@/components/providers";
import "./globals.css";

const inter = Inter({
  subsets: ["latin"],
  variable: "--font-inter",
  display: "swap",
});

export const metadata: Metadata = {
  title: "All Citizens — Ouvidoria",
  description:
    "Plataforma municipal de atendimento e ouvidoria — All Citizens.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="pt-BR" className={`light ${inter.variable} h-full`}>
      <head>
        {/* Material Symbols: sem variante em next/font; carregamento global para ícones do painel/login */}
        {/* eslint-disable-next-line @next/next/no-page-custom-font */}
        <link
          rel="stylesheet"
          href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,300,0,0&display=swap"
        />
      </head>
      <body className="min-h-full bg-surface text-on-surface antialiased font-sans">
        <Providers>{children}</Providers>
      </body>
    </html>
  );
}
