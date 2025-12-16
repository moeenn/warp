const BASE_URL = import.meta.env.VITE_API_HOST

type LocalIPResponse = {
    ip: string
}

export async function getLocalIP(): Promise<LocalIPResponse> {
    const url = BASE_URL + "/local-ip"
    const res = await fetch(url)
    return await res.json() as LocalIPResponse
}

type ListFilesResponse = {
    files: string[]
}

export async function listFiles(): Promise<ListFilesResponse> {
    const url = BASE_URL + "/files"
    const res = await fetch(url)
    return await res.json() as ListFilesResponse
}

export async function getFile(filename: string): Promise<void> {
    const url = BASE_URL + "/files/" + filename
    const anchor = document.createElement("a")
    anchor.href = url
    anchor.download = filename
    anchor.target = "_blank"
    document.body.appendChild(anchor)
    anchor.click()
    document.body.removeChild(anchor)
}
