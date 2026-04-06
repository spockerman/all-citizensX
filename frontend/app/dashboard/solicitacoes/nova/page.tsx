import { NovaSolicitacaoForm } from "@/components/dashboard/nova-solicitacao-form";
import type { Metadata } from "next";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Nova solicitação — All Citizens",
};

export default function NovaSolicitacaoPage() {
  const tenantConfigured = Boolean(
    process.env.TENANT_ID && process.env.DEFAULT_SERVICE_ID,
  );

  return (
    <>
      <div className="mb-6">
        <div className="mb-1 flex flex-wrap items-center gap-2 text-xs font-semibold text-gray-500">
          <Link
            href="/dashboard"
            className="transition-colors hover:text-on-surface"
          >
            Painel
          </Link>
          <span className="text-gray-300">/</span>
          <Link
            href="/dashboard/solicitacoes"
            className="transition-colors hover:text-on-surface"
          >
            Solicitações
          </Link>
          <span className="text-gray-300">/</span>
          <span className="text-on-surface">Nova</span>
        </div>
        <h1 className="text-2xl font-bold tracking-tight text-on-surface">
          Nova solicitação
        </h1>
        <p className="mt-1 text-sm text-gray-500">
          Registro de novo protocolo no sistema (API{" "}
          <code className="rounded bg-gray-100 px-1 text-xs">/api/v1/service-requests</code>
          ).
        </p>
      </div>

      <NovaSolicitacaoForm tenantConfigured={tenantConfigured} />
    </>
  );
}
