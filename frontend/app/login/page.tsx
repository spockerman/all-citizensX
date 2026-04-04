import type { Metadata } from "next";
import { LoginForm } from "@/components/login-form";

export const metadata: Metadata = {
  title: "Entrar — All Citizens",
};

export default function LoginPage() {
  return (
    <div className="bg-surface flex min-h-screen flex-col items-center justify-center p-6 antialiased">
      <LoginForm />
      <div className="pointer-events-none fixed left-0 top-0 -z-10 h-full w-full overflow-hidden">
        <div className="absolute -left-[10%] -top-[10%] h-[40%] w-[40%] rounded-full bg-primary/5 blur-[120px]" />
        <div className="absolute -right-[5%] top-[60%] h-[30%] w-[30%] rounded-full bg-secondary/5 blur-[100px]" />
      </div>
      <footer className="mt-auto w-full border-t border-outline-variant/15 bg-surface-container-low md:hidden">
        <div className="mx-auto flex max-w-7xl flex-col items-center justify-between space-y-4 px-8 py-6 md:flex-row md:space-y-0">
          <span className="text-sm text-on-surface-variant">
            © {new Date().getFullYear()} All Citizens / Centro Avareense. Todos os
            direitos reservados.
          </span>
          <div className="flex space-x-6">
            <a
              className="text-sm text-on-surface-variant underline transition-all duration-200 hover:text-on-surface"
              href="#"
            >
              Privacidade
            </a>
            <a
              className="text-sm text-on-surface-variant underline transition-all duration-200 hover:text-on-surface"
              href="#"
            >
              Termos de uso
            </a>
          </div>
        </div>
      </footer>
    </div>
  );
}
