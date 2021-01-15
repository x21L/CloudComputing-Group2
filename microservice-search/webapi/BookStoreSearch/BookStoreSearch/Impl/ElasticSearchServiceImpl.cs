using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;
using BookStoreSearch.Contract;
using BookStoreSearch.Entity;
using BookStoreSearch.Exception;
using Newtonsoft.Json.Linq;

namespace BookStoreSearch.Impl
{
    public class ElasticSearchServiceImpl<T> : ISearchService<T> where T : IStoreable
    {
        // https://www.elastic.co/de/blog/a-practical-introduction-to-elasticsearch
        private const string IndexName = "bookindex";
        private const string BaseUrl = "http://10.8.7.122:9200";
       // private const string BaseUrl = "https://127.0.0.1:9200";
        private const int TimeOutSeconds = 20;
        
        /// <summary>
        /// Performs a search request in elastic search.
        /// </summary>
        /// <param name="query">Search query.</param>
        /// <param name="settings"><see cref="SearchSettings"/> settings.</param>
        /// <returns>List of search results.</returns>
        public async Task<List<T>> Search(string query, SearchSettings settings)
        {
            using var client = CreateHttpClient();
            var url = BaseUrl + "/_search?q=" + query + "&from=" + settings.From + "&size=" + settings.Size +
                      "&filter_path=hits.total,hits.max_score,hits.hits._id,hits.hits._source";
            var result = await client.GetAsync(url);

            if (!result.IsSuccessStatusCode)
            {
                throw new BusinessException("Searching items has resulted in an invalid status code.");
            }

            var stringContent = await result.Content.ReadAsStringAsync();
            var jObject = Newtonsoft.Json.JsonConvert.DeserializeObject<JObject>(stringContent);

            if (jObject["hits"]["total"]["value"].ToObject<Int32>() == 0)
            {
                return new List<T>();
            }
            
            var jArray = jObject["hits"]["hits"].Select(e => e["_source"]).ToArray();
            var results = jArray.Select(e => Newtonsoft.Json.JsonConvert.DeserializeObject<T>(e.ToString()))
                .ToList();
            return results;
        }

        /// <summary>
        /// Adds or updates an <see cref="IStoreable"/> in Elastic Search depending on if the id is set or not.
        /// </summary>
        /// <param name="item"><see cref="IStoreable"/></param>
        /// <returns><see cref="Task"/></returns>
        public async Task<T> AddOrUpdate(T item)
        {
            if (item.Id == null)
            {
                await Add(item);
                return item;
            }

            using var client = CreateHttpClient();
            var url = BaseUrl + "/" + IndexName + "/_doc/" + item.Id;
            var payload = JsonSerializer.Serialize(item);
            var result = await client.PutAsync(url, new StringContent(payload, Encoding.UTF8, "application/json"));

            if (!result.IsSuccessStatusCode)
            {
                throw new BusinessException("Updating item has resulted in an invalid status code.");
            }

            return item;
        }

        /// <summary>
        /// Deletes the item with the given id.
        /// </summary>
        /// <param name="id">Id of the item.</param>
        /// <returns><see cref="Task"/></returns>
        public async Task Delete(string id)
        {
            using var client = CreateHttpClient();
            var url = BaseUrl + "/" + IndexName + "/_doc/" + id;
            var result = await client.DeleteAsync(url);

            if (!result.IsSuccessStatusCode)
            {
                throw new BusinessException("Deleting item has resulted in an invalid status code.");
            }
        }

        /// <summary>
        /// Gets the item with the given id.
        /// </summary>
        /// <param name="id">Id of the item.</param>
        /// <returns><see cref="Task"/></returns>
        public async Task<T> Get(string id)
        {
            using var client = CreateHttpClient();
            var url = BaseUrl + "/" + IndexName + "/_doc/" + id + "/_source";
            var response = await client.GetAsync(url);

            if (!response.IsSuccessStatusCode)
            {
                if (response.StatusCode == HttpStatusCode.NotFound)
                {
                    return default;
                }
                
                throw new BusinessException("Getting item has resulted in an invalid status code.");
            }

            var stringContent = await response.Content.ReadAsStringAsync();
            var result = JsonSerializer.Deserialize<T>(stringContent);
            return result;
        }

        /// <summary>
        /// Adds a new item to elastic search.
        /// </summary>
        /// <param name="item"><see cref="IStoreable"/></param>
        /// <returns><see cref="Task"/></returns>
        private async Task Add(T item)
        {
            item.Id = Guid.NewGuid().ToString();

            using var client = CreateHttpClient();
            var url = BaseUrl + "/" + IndexName + "/_doc/" + item.Id;
            var payload = JsonSerializer.Serialize(item);
            var result = await client.PostAsync(url, new StringContent(payload, Encoding.UTF8, "application/json"));

            if (!result.IsSuccessStatusCode)
            {
                throw new BusinessException("Adding item has resulted in an invalid status code.");
            }
        }

        private HttpClient CreateHttpClient()
        {
            var handler = new HttpClientHandler
            {
                ServerCertificateCustomValidationCallback = (httpRequestMessage, cert, cetChain, policyErrors) => true
            };

            return new HttpClient(handler) { Timeout = TimeSpan.FromSeconds(TimeOutSeconds) };
        }
    }
}
