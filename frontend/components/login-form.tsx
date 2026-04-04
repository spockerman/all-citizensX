"use client";

import { signIn } from "next-auth/react";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { useSession } from "next-auth/react";

export function LoginForm() {
  const { status } = useSession();
  const router = useRouter();
  const [busy, setBusy] = useState(false);

  useEffect(() => {
    if (status === "authenticated") {
      router.replace("/dashboard");
    }
  }, [status, router]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setBusy(true);
    try {
      await signIn("keycloak", { callbackUrl: "/dashboard" });
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="relative z-10 w-full max-w-[440px] flex flex-col items-center">
      <p className="mb-6 text-center text-sm text-on-surface-variant max-w-sm">
        Acesso ao sistema de ouvidoria e atendimento ao cidadão. A autenticação
        é feita pelo <strong className="text-on-surface">Keycloak</strong> (SSO).
      </p>
      <div className="w-full rounded-xl border border-outline-variant/10 bg-surface-container-lowest p-8 shadow-[0_32px_64px_-12px_rgba(44,52,55,0.06)] md:p-10">
        <form className="space-y-6" onSubmit={handleSubmit}>
          <div className="space-y-2">
            <label
              className="ml-1 block text-[11px] font-bold uppercase tracking-wider text-on-surface-variant"
              htmlFor="email"
            >
              E-mail
            </label>
            <div className="relative">
              <input
                className="w-full rounded-md border-none bg-surface-container-low px-4 py-3.5 text-sm text-on-surface outline-none transition-all placeholder:text-outline/60 focus:ring-2 focus:ring-primary/20"
                id="email"
                name="email"
                autoComplete="username"
                placeholder="nome@orgao.gov.br"
                type="email"
                disabled={busy}
              />
            </div>
          </div>
          <div className="space-y-2">
            <div className="ml-1 flex items-center justify-between">
              <label
                className="block text-[11px] font-bold uppercase tracking-wider text-on-surface-variant"
                htmlFor="password"
              >
                Senha
              </label>
              <a
                className="text-[11px] font-semibold text-primary hover:underline"
                href="#"
                onClick={(ev) => ev.preventDefault()}
              >
                Esqueceu?
              </a>
            </div>
            <div className="relative">
              <input
                className="w-full rounded-md border-none bg-surface-container-low px-4 py-3.5 text-sm text-on-surface outline-none transition-all placeholder:text-outline/60 focus:ring-2 focus:ring-primary/20"
                id="password"
                name="password"
                autoComplete="current-password"
                placeholder="••••••••"
                type="password"
                disabled={busy}
              />
            </div>
          </div>
          <p className="text-xs text-on-surface-variant/90">
            O envio abre a página de login do provedor de identidade; campos
            acima são opcionais para o fluxo visual e podem ser integrados
            depois se necessário.
          </p>
          <div className="ml-1 flex items-center space-x-3">
            <input
              className="h-4 w-4 cursor-pointer rounded border-outline-variant bg-surface-container-low text-primary focus:ring-primary/20"
              id="remember"
              name="remember"
              type="checkbox"
              disabled={busy}
            />
            <label
              className="cursor-pointer select-none text-sm font-medium text-on-surface-variant"
              htmlFor="remember"
            >
              Lembrar-me neste dispositivo
            </label>
          </div>
          <button
            className="group flex w-full items-center justify-center gap-2 rounded-md bg-primary px-6 py-4 text-sm font-bold tracking-wide text-on-primary transition-all hover:bg-primary-dim active:scale-[0.98] disabled:opacity-60"
            type="submit"
            disabled={busy || status === "loading"}
          >
            {busy ? "Redirecionando…" : "Entrar"}
            <span className="material-symbols-outlined text-lg transition-transform group-hover:translate-x-1">
              arrow_forward
            </span>
          </button>
        </form>
      </div>
      <p className="mt-8 text-sm font-medium text-on-surface-variant">
        Sem credenciais?
        <a
          className="ml-1 font-bold text-primary hover:underline"
          href="#"
          onClick={(ev) => ev.preventDefault()}
        >
          Solicite ao administrador
        </a>
      </p>
    </main>
  );
}
