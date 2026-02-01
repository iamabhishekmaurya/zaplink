"use client"

import { useState } from "react"
import { BioPageManager } from '@/features/bio-page/ui/bio-page-manager'
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { BioPagePreview } from '@/features/bio-page/ui/bio-page-preview'
import { BioPageSettings } from '@/features/bio-page/ui/bio-page-settings'

export default function BioPagePage() {
  const [activeTab, setActiveTab] = useState("manage")
  const [selectedPageId, setSelectedPageId] = useState<string | null>(null)
  const [refreshKey, setRefreshKey] = useState(0)

  const handlePageSelect = (pageId: string) => {
    setSelectedPageId(pageId)
    setActiveTab("preview")
  }

  const handlePageUpdate = () => {
    setRefreshKey(prev => prev + 1)
  }

  return (
    <div className="flex-1 space-y-4 p-4 md:p-8 pt-6">
      <div className="flex items-center justify-between space-y-2">
        <h2 className="text-3xl font-bold tracking-tight">Bio Page Manager</h2>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-4">
        <TabsList>
          <TabsTrigger value="manage">Manage Pages</TabsTrigger>
          <TabsTrigger value="preview" disabled={!selectedPageId}>
            Preview
          </TabsTrigger>
          <TabsTrigger value="settings" disabled={!selectedPageId}>
            Settings
          </TabsTrigger>
        </TabsList>

        <TabsContent value="manage" className="space-y-4">
          <BioPageManager 
            onPageSelect={handlePageSelect}
            onPageUpdate={handlePageUpdate}
            key={refreshKey}
          />
        </TabsContent>

        <TabsContent value="preview" className="space-y-4">
          {selectedPageId && (
            <Card>
              <CardHeader>
                <CardTitle>Preview</CardTitle>
              </CardHeader>
              <CardContent>
                <BioPagePreview pageId={selectedPageId} />
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="settings" className="space-y-4">
          {selectedPageId && (
            <Card>
              <CardHeader>
                <CardTitle>Page Settings</CardTitle>
              </CardHeader>
              <CardContent>
                <BioPageSettings 
                  pageId={selectedPageId}
                  onPageUpdate={handlePageUpdate}
                />
              </CardContent>
            </Card>
          )}
        </TabsContent>
      </Tabs>
    </div>
  )
}
