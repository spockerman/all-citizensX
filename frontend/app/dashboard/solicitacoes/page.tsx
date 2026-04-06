import { SolicitacoesTable } from "@/components/dashboard/solicitacoes-table";
import { fetchServiceRequestsList } from "@/lib/service-requests-server";
import type { Metadata } from "next";
import Link from "next/link";

export const metadata: Metadata = {
  title: "Solicitações — All Citizens",
};

function listFetchHint(
  result: Awaited<ReturnType<typeof fetchServiceRequestsList>>,
): string | null {
  if (result.ok) return null;
  switch (result.reason) {
    case "no_token":
      return "A sessão não possui token de acesso do Keycloak. Faça logout e login novamente após configurar os callbacks JWT em Auth.js.";
    case "no_tenant":
      return "Defina a variável TENANT_ID no .env do frontend para listar os protocolos do tenant na API.";
    case "upstream":
      return `Não foi possível carregar os protocolos na API (HTTP ${result.status ?? "—"}). Verifique se o backend está em execução e se o token tem permissão.`;
    default:
      return null;
  }
}

export default async function SolicitacoesPage() {
  const result = await fetchServiceRequestsList(0, 50);
  const rows = result.ok ? result.rows : [];
  const total = result.ok ? result.total : 0;
  const fetchHint = listFetchHint(result);

  return (
    <>
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
        <div>
          <div className="mb-1 flex items-center gap-2 text-xs font-semibold text-gray-500">
            <Link
              href="/dashboard"
              className="transition-colors hover:text-on-surface"
            >
              Painel
            </Link>
            <span className="text-gray-300">/</span>
            <span className="text-on-surface">Solicitações</span>
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-on-surface">
            Solicitações
          </h1>
          <p className="mt-1 text-sm text-gray-500">
            Protocolos do tenant configurado na API.
          </p>
        </div>
        <Link
          href="/dashboard/solicitacoes/nova"
          className="inline-flex items-center justify-center gap-2 rounded-lg bg-gray-900 px-4 py-2.5 text-sm font-bold text-white shadow-sm transition-opacity hover:opacity-90"
        >
          <span className="material-symbols-outlined text-[20px]">add</span>
          Nova solicitação
        </Link>
      </div>

      <SolicitacoesTable rows={rows} total={total} fetchHint={fetchHint} />
    </>
  );
}
