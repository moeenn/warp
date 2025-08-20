import { Spinner, Dropdown, DropdownItem } from "flowbite-react"
import { useEffect, useReducer, useRef } from "react"
import { getFile, getLocalIP, listFiles } from "./lib/api"
import { useAPI } from "./lib/hooks/useAPI"
import { FiUploadCloud, FiDownload, FiRefreshCw, FiMoreVertical } from "react-icons/fi";
import { FileCard } from "./components/FileCard";
import { initialState, reducer } from "./lib/reducer/homeReducer";

export function App() {
  const [state, dispatch] = useReducer(reducer, initialState)
  const uploadFileRef = useRef<HTMLInputElement>(null)
  const localIPRequest = useAPI(getLocalIP)
  const listFilesRequest = useAPI(listFiles)
  const getFileRequest = useAPI(getFile)

  useEffect(() => {
    localIPRequest.call(null)
    listFilesRequest.call(null)
  }, [])

  return (
    <div className="container mx-auto p-4 md:p-6 h-screen flex flex-col">
      <div className="flex flex-row justify-between mb-4">
        <div className="flex space-x-3">
          <h1 className="text-2xl font-bold my-auto">Warp</h1>

          {localIPRequest.request.loading && (
            <span className="my-auto">
              <Spinner size="sm" />
            </span>
          )}

          {(!localIPRequest.request.loading && localIPRequest.request.state === "success") && (
            <span className="bg-blue-50 text-blue-700 text-sm rounded-full py-1 px-3 my-auto">{localIPRequest.request.data.ip}</span>
          )}
        </div>

        <div className="inline-flex md:hidden hover:bg-gray-100 transition-colors rounded">
          <Dropdown label="" dismissOnClick={false} renderTrigger={() => (
            <span className="p-2 cursor-pointer rounded">
              <FiMoreVertical size={20} color="gray" className="my-auto" />
            </span>
          )}>
            <DropdownItem disabled={state.selectedFile === null} onClick={() => getFileRequest.call(state.selectedFile!)}>Download</DropdownItem>
            <DropdownItem onClick={() => uploadFileRef.current?.click()}>Upload</DropdownItem>
            <DropdownItem onClick={() => listFilesRequest.call(null)}>Refresh</DropdownItem>
          </Dropdown>

        </div>

        <div className="hidden md:space-x-2 md:flex">
          <button
            title="Download"
            className="flex space-x-2 hover:bg-slate-50 hover:text-blue-700 rounded px-3 py-2 cursor-pointer disabled:cursor-not-allowed transition-colors disabled:opacity-50"
            disabled={state.selectedFile === null}
            onClick={() => getFileRequest.call(state.selectedFile!)}
          >
            <FiDownload size={20} color="gray" className="my-auto" />
            <span className="text-sm text-slate-800 my-auto">Download</span>
          </button>

          <button title="upload" className="flex cursor-pointer space-x-2 hover:bg-slate-50 hover:text-blue-700 rounded px-3 py-2 transition-colors" onClick={() => uploadFileRef.current?.click()}>
            <FiUploadCloud size={20} color="gray" className="my-auto" />
            <span className="text-sm text-slate-800 my-auto">Upload</span>
            <input type="file" className="hidden" name="file" ref={uploadFileRef} />
          </button>

          <button title="refresh" className="flex cursor-pointer space-x-2 hover:bg-slate-50 hover:text-blue-700 rounded px-3 py-2 transition-colors" onClick={() => listFilesRequest.call(null)}>
            <FiRefreshCw size={20} color="gray" className="my-auto" />
            <span className="text-sm text-slate-800 my-auto">Refresh</span>
          </button>
        </div>
      </div>

      <div className="flex-1 rounded overflow-y-auto">
        {listFilesRequest.request.loading && (
          <div className="h-full flex">
            <span className="m-auto">
              <Spinner size="md" />
            </span>
          </div>
        )}


        {!listFilesRequest.request.loading && listFilesRequest.request.state == "success" && (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4 w-full">
            {listFilesRequest.request.data.files.map(file => (
              <FileCard onClick={() => dispatch({ type: "toggle-selected-file", filename: file })}
                key={file}
                filename={file}
                selected={state.selectedFile === file}
              />
            ))}
          </div>
        )}

      </div>
    </div>
  )
}
