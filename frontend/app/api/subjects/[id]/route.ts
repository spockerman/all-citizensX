import { auth } from "@/auth";
import { NextResponse } from "next/server";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:9080";

type RouteContext = { params: Promise<{ id: string }> };

export async function PUT(req: Request, context: RouteContext) {
  const session = await auth();
  if (!session?.accessToken) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  const { id } = await context.params;

  let body: Record<string, unknown>;
  try {
    body = await req.json();
  } catch {
    return NextResponse.json({ error: "Invalid JSON" }, { status: 400 });
  }

  const name = typeof body.name === "string" ? body.name.trim() : "";
  if (!name) {
    return NextResponse.json({ error: "Name is required" }, { status: 400 });
  }

  const departmentIdRaw = body.departmentId;
  const departmentId =
    typeof departmentIdRaw === "string" && departmentIdRaw.trim().length > 0
      ? departmentIdRaw.trim()
      : null;

  const payload: Record<string, unknown> = {
    name,
    active: Boolean(body.active),
    visibleWeb: Boolean(body.visibleWeb),
    visibleApp: Boolean(body.visibleApp),
  };
  if (departmentId) {
    payload.departmentId = departmentId;
  }

  const res = await fetch(`${API_URL}/api/v1/subjects/${encodeURIComponent(id)}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${session.accessToken}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    const detail = await res.text();
    return NextResponse.json(
      { error: "Failed to update subject", detail },
      { status: res.status },
    );
  }

  const data = (await res.json()) as unknown;
  return NextResponse.json(data);
}

export async function DELETE(_req: Request, context: RouteContext) {
  const session = await auth();
  if (!session?.accessToken) {
    return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
  }

  const { id } = await context.params;

  const res = await fetch(`${API_URL}/api/v1/subjects/${encodeURIComponent(id)}`, {
    method: "DELETE",
    headers: { Authorization: `Bearer ${session.accessToken}` },
  });

  if (res.status === 404) {
    return NextResponse.json({ error: "Subject not found" }, { status: 404 });
  }

  if (!res.ok) {
    const detail = await res.text();
    return NextResponse.json(
      { error: "Failed to delete subject", detail },
      { status: res.status },
    );
  }

  return new NextResponse(null, { status: 204 });
}
