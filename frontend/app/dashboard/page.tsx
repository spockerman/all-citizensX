import type { Metadata } from "next";
import Link from "next/link";
import { fetchServiceRequestsList } from "@/lib/service-requests-server";

export const metadata: Metadata = {
  title: "Painel — All Citizens",
};

export default async function DashboardPage() {
  const listResult = await fetchServiceRequestsList(0, 5);
  const recentRows = listResult.ok ? listResult.rows : [];
  const recentHint = listResult.ok
    ? null
    : listResult.reason === "no_token"
      ? "Token de API indisponível na sessão."
      : listResult.reason === "no_tenant"
        ? "Configure TENANT_ID para ver solicitações reais."
        : "Não foi possível carregar solicitações da API.";
  return (
    <>
      <div className="mb-4 grid grid-cols-1 gap-4 md:grid-cols-4">
        <div className="rounded-xl border border-gray-100 bg-white p-5">
          <div className="mb-1 text-xs font-semibold text-gray-500">
            Índice de resposta (SLA)
          </div>
          <div className="mb-4 flex items-end gap-2">
            <span className="text-2xl font-bold">86</span>
            <span className="mb-1 text-xs text-gray-400">/ 100</span>
          </div>
          <div className="mt-2 flex items-center justify-between border-t border-gray-50 pt-4">
            <div className="flex items-center text-[11px] font-bold text-accent">
              <span className="material-symbols-outlined mr-1 text-[14px]">
                north_east
              </span>
              meta trimestral
            </div>
            <span className="text-[11px] text-gray-400">Dados de exemplo</span>
          </div>
        </div>
        <div className="rounded-xl border border-gray-100 bg-white p-5">
          <div className="flex justify-between">
            <div>
              <div className="mb-1 text-xs font-semibold text-gray-500">
                Solicitações (mês)
              </div>
              <div className="text-2xl font-bold">312</div>
            </div>
            <div className="h-10 w-16">
              <svg className="h-full w-full" viewBox="0 0 100 40">
                <path
                  d="M0 35 Q 20 10, 40 25 T 100 5"
                  fill="none"
                  stroke="var(--color-accent)"
                  strokeWidth="2"
                />
              </svg>
            </div>
          </div>
          <div className="mt-2 flex items-center justify-between border-t border-gray-50 pt-4">
            <div className="flex items-center text-[11px] font-bold text-accent">
              <span className="material-symbols-outlined mr-1 text-[14px]">
                north_east
              </span>
              +8% vs. mês anterior
            </div>
            <span className="text-[11px] text-gray-400">Exemplo</span>
          </div>
        </div>
        <div className="rounded-xl border border-gray-100 bg-white p-5">
          <div className="flex justify-between">
            <div>
              <div className="mb-1 text-xs font-semibold text-gray-500">
                Pendentes de resposta
              </div>
              <div className="text-2xl font-bold">47</div>
            </div>
            <div className="h-10 w-16">
              <svg className="h-full w-full" viewBox="0 0 100 40">
                <path
                  d="M0 38 L 20 30 L 40 35 L 60 20 L 80 25 L 100 15"
                  fill="none"
                  stroke="#2c3437"
                  strokeWidth="2"
                />
              </svg>
            </div>
          </div>
          <div className="mt-2 flex items-center justify-between border-t border-gray-50 pt-4">
            <div className="flex items-center text-[11px] font-bold text-gray-500">
              filas por departamento
            </div>
            <span className="text-[11px] text-gray-400">Exemplo</span>
          </div>
        </div>
        <div className="rounded-xl border border-gray-100 bg-white p-5">
          <div className="flex justify-between">
            <div>
              <div className="mb-1 text-xs font-semibold text-gray-500">
                Novos cadastros
              </div>
              <div className="text-2xl font-bold">28</div>
            </div>
            <div className="h-10 w-16">
              <svg className="h-full w-full" viewBox="0 0 100 40">
                <path
                  d="M0 35 Q 30 5, 50 25 T 100 10"
                  fill="none"
                  stroke="#2c3437"
                  strokeWidth="2"
                />
              </svg>
            </div>
          </div>
          <div className="mt-2 flex items-center justify-between border-t border-gray-50 pt-4">
            <div className="flex items-center text-[11px] font-bold text-accent">
              <span className="material-symbols-outlined mr-1 text-[14px]">
                north_east
              </span>
              cidadãos ativos
            </div>
            <span className="text-[11px] text-gray-400">Exemplo</span>
          </div>
        </div>
      </div>

      <div className="mb-4 grid grid-cols-1 gap-4 lg:grid-cols-5">
        <div className="rounded-xl border border-gray-100 bg-white p-6 lg:col-span-3">
          <div className="mb-8 flex flex-wrap items-start justify-between gap-4">
            <div>
              <div className="mb-1 text-xs font-semibold uppercase tracking-wider text-gray-500">
                Volume de solicitações
              </div>
              <div className="text-3xl font-bold">1.248</div>
              <div className="mt-1 text-xs text-gray-400">
                Acumulado no período (dados ilustrativos)
              </div>
            </div>
            <div className="flex items-center gap-6">
              <div className="flex items-center gap-4 text-[10px] font-bold uppercase tracking-widest text-gray-400">
                <div className="flex items-center gap-2">
                  <div className="h-2 w-2 rounded-full bg-on-background" />{" "}
                  Trim. atual
                </div>
                <div className="flex items-center gap-2">
                  <div className="h-2 w-2 rounded-full bg-gray-200" /> Anterior
                </div>
              </div>
              <select
                className="rounded-lg border-none bg-gray-50 px-3 py-1 text-xs font-bold focus:ring-0"
                disabled
                aria-label="Período"
              >
                <option>Trimestre</option>
              </select>
            </div>
          </div>
          <div className="relative h-64">
            <div className="absolute inset-0 flex flex-col justify-between opacity-10">
              <div className="w-full border-t border-gray-900" />
              <div className="w-full border-t border-gray-900" />
              <div className="w-full border-t border-gray-900" />
              <div className="w-full border-t border-gray-900" />
            </div>
            <svg
              className="h-full w-full"
              preserveAspectRatio="none"
              viewBox="0 0 1000 200"
            >
              <path
                d="M0,150 C100,130 150,140 200,100 S300,160 400,120 S500,80 600,90 S700,130 800,70 S900,50 1000,40"
                fill="none"
                stroke="#1a1c1e"
                strokeWidth="2"
              />
              <path
                d="M0,160 C100,150 150,155 200,130 S300,180 400,150 S500,110 600,120 S700,160 800,110 S900,100 1000,95"
                fill="none"
                stroke="#e5e7eb"
                strokeDasharray="4"
                strokeWidth="2"
              />
            </svg>
            <div className="absolute bottom-0 flex w-full justify-between px-1 text-[10px] font-bold text-gray-400">
              <span>S1</span>
              <span>S2</span>
              <span>S3</span>
              <span>S4</span>
              <span>S5</span>
              <span>S6</span>
            </div>
          </div>
        </div>

        <div className="rounded-xl border border-gray-100 bg-white p-6 lg:col-span-2">
          <div className="mb-8 flex flex-wrap items-start justify-between gap-4">
            <div>
              <div className="mb-1 text-xs font-semibold uppercase tracking-wider text-gray-500">
                Por canal de entrada
              </div>
              <div className="text-3xl font-bold">4</div>
            </div>
            <select
              className="rounded-lg border-none bg-gray-50 px-3 py-1 text-xs font-bold focus:ring-0"
              disabled
              aria-label="Ano"
            >
              <option>Ano</option>
            </select>
          </div>
          <div className="flex h-64 items-end gap-2">
            {[45, 62, 38, 71, 55, 80, 48, 66].map((h, i) => (
              <div
                key={i}
                className="flex h-full min-h-0 flex-1 items-end justify-center"
              >
                <div
                  className="w-full rounded-t-sm bg-accent shadow-sm transition-opacity hover:opacity-90"
                  style={{ height: `${h}%` }}
                />
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-4 lg:grid-cols-4">
        <div className="rounded-xl border border-gray-100 bg-white p-6 lg:col-span-3">
          <div className="mb-6 flex flex-wrap items-center justify-between gap-3">
            <h2 className="text-xl font-bold">Solicitações recentes</h2>
            <div className="flex flex-wrap items-center gap-2">
              <Link
                href="/dashboard/solicitacoes"
                className="flex items-center gap-1 rounded-lg border border-gray-200 px-4 py-2 text-xs font-bold text-on-surface transition-colors hover:bg-gray-50"
              >
                <span className="material-symbols-outlined text-sm">
                  list_alt
                </span>
                Ver todas
              </Link>
              <Link
                href="/dashboard/solicitacoes/nova"
                className="flex items-center gap-2 rounded-lg bg-gray-900 px-4 py-2 text-xs font-bold text-white transition-opacity hover:opacity-90"
              >
                <span className="material-symbols-outlined text-sm">add</span>
                Nova solicitação
              </Link>
            </div>
          </div>
          {recentHint ? (
            <p className="mb-4 rounded-lg border border-amber-100 bg-amber-50/80 px-3 py-2 text-xs text-amber-950">
              {recentHint}
            </p>
          ) : (
            <p className="mb-4 text-xs text-gray-400">
              Até cinco solicitações mais recentes do tenant configurado.
            </p>
          )}
          <div className="space-y-2">
            {recentRows.length === 0 && !recentHint ? (
              <div className="rounded-xl border border-dashed border-gray-200 bg-gray-50/50 px-4 py-8 text-center text-sm text-gray-500">
                Nenhuma solicitação encontrada.{" "}
                <Link
                  href="/dashboard/solicitacoes/nova"
                  className="font-bold text-accent underline-offset-2 hover:underline"
                >
                  Registrar a primeira
                </Link>
                .
              </div>
            ) : null}
            {recentRows.map((row) => (
              <div
                key={row.id}
                className="flex flex-col gap-3 rounded-xl p-4 transition-colors hover:bg-gray-50/50 sm:flex-row sm:items-center sm:justify-between"
              >
                <div>
                  <div className="flex flex-wrap items-center gap-2">
                    <span className="font-mono text-sm font-bold text-on-surface">
                      {row.protocol}
                    </span>
                    <span className="h-2 w-2 rounded-full bg-accent" />
                    <span className="text-xs font-bold text-gray-600">
                      {row.statusLabel ?? "—"}
                    </span>
                  </div>
                  <div className="mt-1 line-clamp-2 text-sm font-semibold text-on-surface">
                    {row.description ?? "Sem descrição"}
                  </div>
                  <div className="mt-1 flex items-center text-xs text-gray-500">
                    <span className="material-symbols-outlined mr-1 text-sm">
                      outreach
                    </span>
                    {row.channelLabel ?? "—"} · atualizado {row.updatedLabel}
                  </div>
                </div>
                <div className="flex gap-2 sm:justify-end">
                  <Link
                    href="/dashboard/solicitacoes"
                    className="flex items-center gap-1 rounded-lg border border-gray-200 px-3 py-1.5 text-[11px] font-bold hover:bg-white"
                  >
                    <span className="material-symbols-outlined text-[14px]">
                      list_alt
                    </span>
                    Lista
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </div>

        <div className="flex flex-col rounded-xl border border-gray-100 bg-white p-6 lg:col-span-1">
          <div className="mb-8 flex items-center justify-between">
            <h2 className="text-lg font-bold">Filas por departamento</h2>
          </div>
          <div className="flex flex-1 flex-col space-y-6">
            {[
              { name: "Ouvidoria", value: 82 },
              { name: "Serviços urbanos", value: 64 },
              { name: "Assistência social", value: 51 },
            ].map((d) => (
              <div key={d.name}>
                <div className="mb-2 flex items-center justify-between">
                  <span className="text-xs font-bold">{d.name}</span>
                  <span className="text-xs font-bold text-gray-500">
                    {d.value}%
                  </span>
                </div>
                <div className="h-1.5 w-full overflow-hidden rounded-full bg-gray-100">
                  <div
                    className="h-full rounded-full bg-gray-900"
                    style={{ width: `${d.value}%` }}
                  />
                </div>
              </div>
            ))}
          </div>
          <p className="mt-6 text-[11px] text-gray-400">
            Percentagens ilustrativas de distribuição de demanda.
          </p>
        </div>
      </div>
    </>
  );
}
